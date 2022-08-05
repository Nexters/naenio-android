package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.data.network.dto.FeedRequest
import com.nexters.teamvs.naenio.repository.CommentRepository
import com.nexters.teamvs.naenio.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    init {
        getFeedPosts()
    }

    fun getFeedPosts() {
        viewModelScope.launch {
            try {
                feedRepository.getFeedPosts(
                    pageSize = 10,
                    lastPostId = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}