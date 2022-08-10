package com.nexters.teamvs.naenio.ui.comment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
) : BaseViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    fun loadFirstComments(postId: Int) {
        viewModelScope.launch {
            try {
                val commentList = commentRepository.getComments(
                    postId = postId,
                    size = 10,
                    lastCommentId = null
                )
                _comments.value = commentList

                Log.d(className, "Request PostId: $postId")
                Log.d(className, "Loaded CommentList size: ${commentList.size} , Current Comments size: ${comments.value.size}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var lastKey: Int = -1
    fun loadMoreComments(postId: Int, lastCommentId: Int) {
        val alreadyIsRequested = lastKey == lastCommentId
        if (alreadyIsRequested) return

        viewModelScope.launch {
            try {
                val commentList = commentRepository.getComments(
                    postId = postId,
                    size = 10,
                    lastCommentId = lastCommentId
                )
                _comments.value = comments.value + commentList

                lastKey = lastCommentId

                Log.d(className, "Request PostId: $postId , lastCommendId: $lastCommentId")
                Log.d(className, "Loaded CommentList size: ${commentList.size} , Current Comments size: ${comments.value.size}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun writeComment(
        postId: Int,
        content: String,
    ) {
        viewModelScope.launch {
            try {
                val comment = commentRepository.writeComment(
                    postId = postId,
                    content = content,
                )

                _comments.value = listOf(comment) + comments.value
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun writeReply(
        commentId: Int,
        content: String,
    ) {
        viewModelScope.launch {
            try {
                commentRepository.writeReply(
                    commentId = commentId,
                    content = content,
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}