package com.nexters.teamvs.naenio.ui.comment

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.ui.model.Reply
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor() : BaseViewModel() {

    private val _replies = MutableStateFlow<List<Reply>>(emptyList())
    val replies = _replies.asStateFlow()

    init {
        getReplies(0)
    }

    fun getReplies(parentId: Int) {
        viewModelScope.launch {
            _replies.emit(Reply.mock)
        }
    }
}