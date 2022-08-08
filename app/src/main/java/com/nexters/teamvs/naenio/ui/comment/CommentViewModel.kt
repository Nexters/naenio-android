package com.nexters.teamvs.naenio.ui.comment

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

    fun getComments(postId: Int) {
        viewModelScope.launch {
            try {
                _comments.value = commentRepository.getComments(
                    postId = postId,
                    size = 10,
                    lastCommentId = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}