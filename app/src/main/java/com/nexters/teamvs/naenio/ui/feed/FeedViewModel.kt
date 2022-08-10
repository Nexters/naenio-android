package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.CommentRepository
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : BaseViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()

    init {
        getFeedPosts()
    }

    fun getFeedPosts() {
        viewModelScope.launch {
            try {
                _posts.value = feedRepository.getFeedPosts(
                    pageSize = 10,
                    lastPostId = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}