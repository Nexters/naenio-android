package com.nexters.teamvs.naenio.ui.comment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.data.network.dto.ReportRequest
import com.nexters.teamvs.naenio.data.network.dto.ReportType
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.feed.paging.PagingSource2
import com.nexters.teamvs.naenio.ui.feed.paging.PlaceholderState
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    userRepository: UserRepository,
) : BaseViewModel(), PagingSource2 {

    companion object {
        val replyCallback = MutableSharedFlow<Comment?>()
        val commentDeleteEvent = MutableSharedFlow<Comment>()
    }

    val commentUiState = mutableStateOf<UiState>(UiState.Idle)
    private val _loadingState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    private val _firstPageState = MutableStateFlow<PlaceholderState>(PlaceholderState.Idle(true))
    val isRefreshing = MutableStateFlow(false)
    private var isFirstPage = true
    private var loadedAllPage = false

    private val _selectedComment = MutableStateFlow<Comment?>(null)
    val selectedComment = _selectedComment.asStateFlow()
    private val _replies = MutableStateFlow<List<Reply>>(emptyList())
    val replies = _replies.asStateFlow()

    val inputUiState = mutableStateOf<UiState>(UiState.Idle)

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

    fun writeReply(
        commentId: Int,
        content: String,
    ) {
        viewModelScope.launch {
            try {
                inputUiState.value = UiState.Loading

                if (content.length > CommentViewModel.MAX_COMMENT_LENGTH) {
                    GlobalUiEvent.showToast("최대 200자까지 입력 가능합니다.")
                    return@launch
                } else if (content.isBlank()) {
                    GlobalUiEvent.showToast("댓글을 입력해주세요.")
                    return@launch
                }

                val reply = commentRepository.writeReply(
                    commentId = commentId,
                    content = content,
                )
                _replies.value = listOf(reply) + replies.value
                val comment = _selectedComment.value
                _selectedComment.value = comment?.copy(
                    replyCount = comment.replyCount + 1
                )
                replyCallback.emit(selectedComment.value)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                inputUiState.value = UiState.Success
            }
        }
    }

    fun delete(reply: Reply) {
        viewModelScope.launch {
            try {
                commentRepository.deleteComment(reply.id)
                _replies.value = replies.value - listOf(reply).toSet()

                val comment = _selectedComment.value
                _selectedComment.value = comment?.copy(
                    replyCount = comment.replyCount - 1
                )
                replyCallback.emit(selectedComment.value)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun like(id: Int) {
        viewModelScope.launch {
            try {
                commentRepository.likeComment(id)
                _replies.value = replies.value.map {
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
                _replies.value = replies.value.map {
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
        _replies.value = emptyList()
        _selectedComment.value = null
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

    private fun loadPageInternal(refresh: Boolean = false, id: Int) {
        val currentReplies = replies.value
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
                    currentReplies.lastOrNull()
                }
                commentRepository.getReplies(
                    commentId = id,
                    size = 10,
                    lastCommentId = lastComment?.id
                )
            }.fold(
                onSuccess = {
                    if (refresh) {
                        _replies.value = emptyList()
                        isRefreshing.value = false
                    } else {
                        updateState(PlaceholderState.Idle(it.isEmpty()))
                    }
                    _replies.value = replies.value + it

                    isFirstPage = false
                    loadedAllPage = it.isEmpty()
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

    override fun loadNextPage(id: Int) {
        if (shouldLoadNextPage) {
            loadPageInternal(id = id)
        }
    }

    override fun retry(id: Int) {
        if (shouldRetry) {
            loadPageInternal(id = id)
        }
    }

    override fun refresh(id: Int) {
        loadPageInternal(refresh = true, id)
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

    fun setSelectedComment(comment: Comment) {
        _selectedComment.value = comment
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            try {
                commentRepository.deleteComment(comment.id)
                _selectedComment.value = null
                commentDeleteEvent.emit(comment)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun likeComment(id: Int) {
        viewModelScope.launch {
            try {
                commentRepository.likeComment(id)
                val comment = _selectedComment.value
                _selectedComment.value = comment?.copy(likeCount = comment.likeCount + 1, isLiked = true)
                replyCallback.emit(selectedComment.value)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun unlikeComment(id: Int) {
        viewModelScope.launch {
            try {
                commentRepository.unlikeComment(id)
                val comment = _selectedComment.value
                _selectedComment.value = comment?.copy(likeCount = comment.likeCount - 1, isLiked = false)
                replyCallback.emit(selectedComment.value)
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }
}