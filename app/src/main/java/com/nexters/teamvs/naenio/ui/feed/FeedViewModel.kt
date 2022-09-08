package com.nexters.teamvs.naenio.ui.feed

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.data.network.dto.ReportRequest
import com.nexters.teamvs.naenio.data.network.dto.ReportType
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.comment.CommentViewModel.Companion.dismissCommentDialog
import com.nexters.teamvs.naenio.ui.feed.paging.PagingSource
import com.nexters.teamvs.naenio.ui.feed.paging.PlaceholderState
import com.nexters.teamvs.naenio.ui.theme.ThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import javax.inject.Inject

sealed class FeedEvent {
    object ScrollToTop : FeedEvent()
    object VoteSuccess: FeedEvent()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    userRepository: UserRepository,
) : BaseViewModel(), PagingSource {

    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts = _posts.asStateFlow()

    private val _postId = MutableStateFlow<Int?>(null)
    val postItem: Flow<Post?> = posts.map { post ->
            post?.find { it.id == _postId.value }
    }
    fun setDetailPostItem(postId: Int?) {
        _postId.value = postId
    }

    private val _loadingState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    private val _firstPageState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    private val _isRefreshing = MutableStateFlow(false)
    private var isFirstPage = true
    private var loadedAllPage = false

    private inline val shouldLoadNextPage: Boolean
        get() = if (isFirstPage) {
            _firstPageState.value is PlaceholderState.Idle
        } else {
            _loadingState.value is PlaceholderState.Idle
        } && !loadedAllPage

    private inline val shouldRetry: Boolean
        get() = if (isFirstPage) {
            _firstPageState.value is PlaceholderState.Failure
        } else {
            _loadingState.value is PlaceholderState.Failure
        }

    val user = userRepository.getUserFlow()

    private val _feedTabItems = MutableStateFlow(feedRepository.getFeedTabItems())
    val feedTabItems = _feedTabItems.asStateFlow()

    private val _selectedTab = MutableStateFlow(feedTabItems.value[0])
    val selectedTab = _selectedTab.asStateFlow()
    fun selectTab(feedTabItemModel: FeedTabItemModel) {
        _selectedTab.value = feedTabItemModel
    }

    val event = MutableSharedFlow<FeedEvent>()
    fun emitEvent(feedEvent: FeedEvent) {
        viewModelScope.launch {
            event.emit(feedEvent)
        }
    }

    init {
        viewModelScope.launch {
            dismissCommentDialog.collect { data ->
                val result =
                    posts.value?.map { if (it.id == data.postId) it.copy(commentCount = data.commentCount) else it }
                _posts.value = result
            }
        }
    }

    fun loadFirstFeed() {
        viewModelScope.launch {
            selectedTab.collect {
                try {
                    GlobalUiEvent.showLoading()
                    getFeedPosts(it.type)
                    emitEvent(FeedEvent.ScrollToTop)
                } catch (e: Exception) {
                    GlobalUiEvent.showToast(e.errorMessage())
                } finally {
                    GlobalUiEvent.hideLoading()
                }
            }
        }
    }

    private suspend fun getFeedPosts(feedTabItemType: FeedTabItemType) {
        _posts.value = feedRepository.getFeedPosts(
            pageSize = 10,
            lastPostId = null,
            sortType = feedTabItemType.text
        )
    }

    @MainThread
    override fun loadNextPage() {
        if (shouldLoadNextPage) {
            loadPageInternal()
        }
    }

    @MainThread
    override fun retry() {
        if (shouldRetry) {
            loadPageInternal()
        }
    }

    @MainThread
    override fun refresh() {
        loadPageInternal(refresh = true)
    }

    @MainThread
    private fun updateState(state: PlaceholderState) {
        if (isFirstPage) {
            _firstPageState.value = state
        } else {
            _loadingState.value = state
        }
    }

    private fun loadPageInternal(refresh: Boolean = false) {
        val currentSortType = selectedTab.value.type
        val currentPosts = posts.value
        viewModelScope.launch {
            if (refresh) {
                _isRefreshing.value = true
            } else {
                updateState(PlaceholderState.Loading)
            }

            val currentList = if (refresh) emptyList() else _posts.value
            Log.d("### lastPostId,", "${currentPosts?.getOrNull(currentPosts.size - 1)?.id}") // 클라에서 중복 필터링?
            runCatching {
                feedRepository.getFeedPosts(
                    pageSize = 10,
                    lastPostId = currentPosts?.getOrNull(currentPosts.size - 1)?.id,
                    sortType = currentSortType.text,
                )
            }.fold(
                onSuccess = {
                    if (refresh) {
                        _isRefreshing.value = false
                    } else {
                        updateState(PlaceholderState.Idle(it.isEmpty()))
                    }
                    _posts.value = currentList?.plus(it)

                    isFirstPage = false
                    loadedAllPage = it.isEmpty()
                },
                onFailure = {
                    if (refresh) {
                        _isRefreshing.value = false
                    } else {
                        updateState(PlaceholderState.Failure(it))
                    }
                }
            )
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                feedRepository.deletePost(postId)
                _posts.value = posts.value?.filter { it.id != postId }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun report(targetMemberId: Int, resourceType: ReportType) {
        viewModelScope.launch {
            try {
                feedRepository.report(
                    ReportRequest(
                        targetMemberId = targetMemberId,
                        resource = resourceType,
                    )
                )
                GlobalUiEvent.showToast("신고 되었습니다.")
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    private var voteLock = false
    fun vote(
        postId: Int,
        choiceId: Int,
    ) {
        if (voteLock) return
        voteLock = true

        val currentPosts = posts.value ?: emptyList()

        /**
         * 같은 선택지에 또 투표를 할 경우, API 요청 막기.
         */
        val post = currentPosts.find { it.id == postId }
        val choice = post?.choices?.find { it.id == choiceId }
        if (post?.isAlreadyVote == true && choice?.isVoted == true) {
            voteLock = false
            return
        }

        viewModelScope.launch {
            try {
                feedRepository.vote(
                    postId = postId,
                    choiceId = choiceId,
                )

                currentPosts.map { post ->
                    if (post.id == postId) {
                        val isVotedForPost = post.isAlreadyVote
                        post.copy(
                            choices = post.choices.map { choice ->
                                if (choice.id == choiceId) {
                                    choice.copy(
                                        isVoted = true,
                                        voteCount = choice.voteCount + 1
                                    )
                                } else choice.copy(
                                    isVoted = false,
                                    voteCount = if (isVotedForPost) choice.voteCount - 1 else choice.voteCount
                                )
                            },
                        )
                    } else post
                }.also {
                    _posts.value = it
                }
                emitEvent(FeedEvent.VoteSuccess)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                voteLock = false
            }
        }
    }

    fun getPostDetail(id: Int) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                val detailPost = feedRepository.getPostDetail(id = id)
                _posts.value = posts.value?.map { if (it.id == detailPost.id) detailPost else it }
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun getThemePosts(type: ThemeType) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _posts.value = feedRepository.getThemePosts(
                    theme = type.name
                )
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun getRandomPost() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _postId.value = feedRepository.getRandomPosts().id
            } catch (e: SerializationException) {
                e.printStackTrace()
                GlobalUiEvent.showToast("랜덤 컨텐츠가 없습니다 ㅜㅜ. 재시도 해주세요!")
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }
}