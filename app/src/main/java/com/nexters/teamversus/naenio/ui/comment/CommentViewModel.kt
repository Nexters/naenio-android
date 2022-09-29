package com.nexters.teamversus.naenio.ui.comment

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nexters.teamversus.naenio.base.BaseViewModel
import com.nexters.teamversus.naenio.base.GlobalUiEvent
import com.nexters.teamversus.naenio.data.network.dto.ReportRequest
import com.nexters.teamversus.naenio.data.network.dto.ReportType
import com.nexters.teamversus.naenio.domain.repository.CommentRepository
import com.nexters.teamversus.naenio.domain.repository.UserRepository
import com.nexters.teamversus.naenio.extensions.errorMessage
import com.nexters.teamversus.naenio.ui.comment.ReplyViewModel.Companion.commentDeleteEvent
import com.nexters.teamversus.naenio.ui.comment.ReplyViewModel.Companion.replyCallback
import com.nexters.teamversus.naenio.ui.feed.paging.PagingSource2
import com.nexters.teamversus.naenio.ui.feed.paging.PlaceholderState
import com.nexters.teamversus.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommentCallbackData(
    val postId: Int,
    val commentCount: Int,
)

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(), PagingSource2 {

    companion object {
        val dismissCommentDialog = MutableSharedFlow<CommentCallbackData>()
        const val MAX_COMMENT_LENGTH = 200
    }

    private val commentPagingSize = 10
    val commentUiState = mutableStateOf<UiState>(UiState.Idle)
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()
    private val _loadingState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    private val _firstPageState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    val isRefreshing = MutableStateFlow(false)
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

    val inputUiState = mutableStateOf<UiState>(UiState.Idle)
    val user = userRepository.getUserFlow()

    val totalCommentCount = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            replyCallback.collect { updatedComment ->
                _comments.value = comments.value.map {
                    if (updatedComment?.id == it.id) updatedComment
                    else it
                }
            }
        }

        viewModelScope.launch {
            commentDeleteEvent.collect { updatedComment ->
                _comments.value = comments.value.mapNotNull {
                    if (updatedComment.id == it.id) null
                    else it
                }
            }
        }
    }
    suspend fun setTotalCommentCount(count: Int, postId: Int) {
        totalCommentCount.value = count

        dismissCommentDialog.emit(
            CommentCallbackData(
                postId = postId,
                commentCount = count
            )
        )
    }

    fun writeComment(
        postId: Int,
        content: String,
    ) {
        viewModelScope.launch {
            try {
                inputUiState.value = UiState.Loading

                if (content.length > MAX_COMMENT_LENGTH) {
                    GlobalUiEvent.showToast("최대 200자까지 입력 가능합니다.")
                    return@launch
                } else if (content.isBlank()) {
                    GlobalUiEvent.showToast("댓글을 입력해주세요.")
                    return@launch
                }

                val comment = commentRepository.writeComment(
                    postId = postId,
                    content = content,
                )

                _comments.value = listOf(comment) + comments.value
                setTotalCommentCount(totalCommentCount.value + 1, postId)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                inputUiState.value = UiState.Success
            }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            try {
                commentRepository.deleteComment(comment.id)
                _comments.value = comments.value - listOf(comment).toSet()
                setTotalCommentCount(totalCommentCount.value + -1, comment.postId)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun like(id: Int) {
        viewModelScope.launch {
            try {
                commentRepository.likeComment(id)
                _comments.value = comments.value.map {
                    if (it.id == id) it.copy(isLiked = true, likeCount = it.likeCount + 1) else it
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun unlike(id: Int) {
        viewModelScope.launch {
            try {
                commentRepository.unlikeComment(id)
                _comments.value = comments.value.map {
                    if (it.id == id) it.copy(isLiked = false, likeCount = it.likeCount - 1) else it
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    /**
     * 바텀시트가 닫혀도, 바텀시트의 Lifecycle 이 Destroy 상태가 되지 않아서 닫힐 때마다 수동으로 clear 처리..
     */
    fun clear() {
        _comments.value = emptyList()

        _loadingState.value = PlaceholderState.Idle(true)
        _firstPageState.value = PlaceholderState.Idle(true)
        isRefreshing.value = false
        isFirstPage = true
        loadedAllPage = false
    }

    private fun updateState(state: PlaceholderState) {
        if (isFirstPage) {
            _firstPageState.value = state
        } else {
            _loadingState.value = state
        }
    }

    private fun loadPageInternal(refresh: Boolean = false, postId: Int) {
        val currentComments = comments.value
        viewModelScope.launch {
            if (refresh) {
                isRefreshing.value = true
            } else {
                updateState(PlaceholderState.Loading)
            }

            runCatching {
                val lastComment = if (refresh || isFirstPage) {
                    null
                } else {
                    currentComments.lastOrNull()
                }

                commentRepository.getComments(
                    postId = postId,
                    size = commentPagingSize,
                    lastCommentId = lastComment?.id
                )
            }.fold(
                onSuccess = {
                    if (refresh) {
                        _comments.value = emptyList()
                        isRefreshing.value = false
                    } else {
                        updateState(PlaceholderState.Idle(it.isEmpty()))
                    }
                    _comments.value = comments.value + it

                    isFirstPage = false
                    loadedAllPage = it.isEmpty() || commentPagingSize > it.size
                },
                onFailure = {
                    if (refresh) {
                        isRefreshing.value = false
                    } else {
                        updateState(PlaceholderState.Failure(it))
                    }
                }
            )
        }
    }

    @MainThread
    override fun loadNextPage(id: Int) {
        if (shouldLoadNextPage) {
            loadPageInternal(postId = id)
        }
    }

    @MainThread
    override fun retry(id: Int) {
        if (shouldRetry) {
            loadPageInternal(postId = id)
        }
    }

    @MainThread
    override fun refresh(id: Int) {
        loadPageInternal(refresh = true, postId = id)
    }

    fun report(targetMemberId: Int, resourceType: ReportType = ReportType.COMMENT) {
        viewModelScope.launch {
            try {
                commentRepository.report(
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

    fun block(userId: Int) {
        viewModelScope.launch {
            try {
                userRepository.block(userId)
                _comments.value = comments.value.filter { it.writer.id != userId }
                GlobalUiEvent.showToast("차단 되었습니다. 해당 유저가 작성하는 게시물과 댓글은 더 이상 보이지 않습니다.")
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }
}