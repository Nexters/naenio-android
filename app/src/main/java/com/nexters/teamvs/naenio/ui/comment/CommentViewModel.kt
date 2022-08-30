package com.nexters.teamvs.naenio.ui.comment

import androidx.annotation.MainThread
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.ui.feed.paging.PagingSource2
import com.nexters.teamvs.naenio.ui.feed.paging.PlaceholderState
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
) : BaseViewModel(), PagingSource2 {

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

    fun writeComment(
        postId: Int,
        content: String,
    ) {
        inputUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val comment = commentRepository.writeComment(
                    postId = postId,
                    content = content,
                )

                _comments.value = listOf(comment) + comments.value
                inputUiState.value = UiState.Success
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            try {
                commentRepository.deleteComment(comment.id)
                _comments.value = comments.value - listOf(comment).toSet()
            } catch (e: Exception) {
                e.printStackTrace()
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
                e.printStackTrace()
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
                e.printStackTrace()
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

            val currentList = if (refresh) emptyList() else comments.value

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
                    _comments.value = currentList + it

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
}