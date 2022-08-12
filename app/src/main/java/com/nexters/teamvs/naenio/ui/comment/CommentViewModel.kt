package com.nexters.teamvs.naenio.ui.comment

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    val commentUiState = mutableStateOf<UiState>(UiState.Idle)
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _replies = MutableStateFlow<List<Reply>>(emptyList())
    val replies = _replies.asStateFlow()

    val inputUiState = mutableStateOf<UiState>(UiState.Idle)

    fun loadFirstComments(postId: Int) {
        commentUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val commentList = commentRepository.getComments(
                    postId = postId,
                    size = 10,
                    lastCommentId = null
                )
                _comments.value = commentList
                Log.d(className, "Request PostId: $postId")
                Log.d(
                    className,
                    "Loaded CommentList size: ${commentList.size} , Current Comments size: ${comments.value.size}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                commentUiState.value = UiState.Success
            }
        }
    }

    var lastKey: Int = -1
    fun loadMoreComments(postId: Int, lastCommentId: Int) {
        val alreadyIsRequested = lastKey == lastCommentId
        if (alreadyIsRequested) return
        lastKey = lastCommentId
        viewModelScope.launch {
            try {
                commentUiState.value = UiState.Loading
                val commentList = commentRepository.getComments(
                    postId = postId,
                    size = 10,
                    lastCommentId = lastCommentId
                )
                _comments.value = comments.value + commentList
                commentUiState.value = UiState.Success
                Log.d(className, "Request PostId: $postId , lastCommendId: $lastCommentId")
                Log.d(
                    className,
                    "Loaded CommentList size: ${commentList.size} , Current Comments size: ${comments.value.size}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                commentUiState.value = UiState.Success
            }
        }
    }

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

    fun loadFirstReplies(commentId: Int) {
        commentUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val replyList = commentRepository.getReplies(
                    commentId = commentId,
                    size = 10,
                    lastCommentId = null
                )
                _replies.value = replyList
                Log.d(className, "Request CommentId: $commentId")
                Log.d(
                    className,
                    "Loaded Reply size: ${replyList.size} , Current Reply size: ${replies.value.size}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                commentUiState.value = UiState.Success
            }
        }
    }

    var lastReplyKey: Int = -1
    fun loadMoreReplies(commentId: Int, lastCommentId: Int) {
        val alreadyIsRequested = lastReplyKey == lastCommentId
        if (alreadyIsRequested) return
        lastReplyKey = lastCommentId
        viewModelScope.launch {
            try {
                commentUiState.value = UiState.Loading
                val replyList = commentRepository.getReplies(
                    commentId = commentId,
                    size = 10,
                    lastCommentId = lastCommentId
                )
                _replies.value = replyList
                commentUiState.value = UiState.Success
                Log.d(className, "Request CommentId: $commentId")
                Log.d(
                    className,
                    "Loaded Reply size: ${replyList.size} , Current Reply size: ${replies.value.size}"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                commentUiState.value = UiState.Success
            }
        }
    }

    fun writeReply(
        commentId: Int,
        content: String,
    ) {
        inputUiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val reply = commentRepository.writeReply(
                    commentId = commentId,
                    content = content,
                )

                _replies.value = listOf(reply) + replies.value
                inputUiState.value = UiState.Success
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}