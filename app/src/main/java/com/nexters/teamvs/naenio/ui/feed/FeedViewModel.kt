package com.nexters.teamvs.naenio.ui.feed

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.feed.paging.PagingSource
import com.nexters.teamvs.naenio.ui.feed.paging.PlaceholderState
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FeedEvent {
    object ScrollToTop : FeedEvent()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : BaseViewModel(), PagingSource {

    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts = _posts.asStateFlow()
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

    private val _themePosts = MutableStateFlow<List<Post>?>(null)
    val themePosts = _themePosts.asStateFlow()

    private val _themeItem = MutableStateFlow<ThemeItem>(ThemeItem())
    val themeItem = _themeItem.asStateFlow()

    private val _postItem = MutableStateFlow<Post?>(null)
    val postItem = _postItem.asStateFlow()

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

    fun setType(type: String) {
        Log.d("#### setType", type)
        if (type.contains("theme")) {
            setThemeItem(type, "theme=")
            getThemePosts(_themeItem.value.type)
        } else if (type.contains("random")) {
            setThemeItem(type, "feedDetail=random=")
            getRandomPost()
        } else if (type.contains("feedDetail")) {
            val id = type.replace("feedDetail=", "").toInt()
            getPostDetail(id)
        }
    }

    private fun setThemeItem(type: String, replaceStr: String) {
        Log.d("### setThemeItem", "${type} // ${replaceStr}")
        _themeItem.value = ThemeItem.themeList[type.replace(replaceStr, "").toInt() - 1]
        Log.d("### setThemeItem", _themeItem.value.type)
    }

    private fun getPostDetail(id: Int) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _postItem.value = feedRepository.getPostDetail(id = id)
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    private fun getThemePosts(type: String) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _themePosts.value = feedRepository.getThemePosts(
                    theme = type
                )
            } catch (e: Exception) {
                e.printStackTrace()
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
                _postItem.value = feedRepository.getRandomPosts()
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
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
        if (post?.isVotedForPost() == true && choice?.isVoted == true) {
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
                        val isVotedForPost = post.isVotedForPost()
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
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                voteLock = false
            }
        }
    }

}