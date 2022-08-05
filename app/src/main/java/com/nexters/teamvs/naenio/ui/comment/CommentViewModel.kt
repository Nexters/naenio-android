package com.nexters.teamvs.naenio.ui.comment

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.ui.model.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor() : BaseViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    init {
        getComments(0)
    }
    fun getComments(postId: Int) {
        viewModelScope.launch {
            _comments.emit(Comment.mock)
        }
    }

}