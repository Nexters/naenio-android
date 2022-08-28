package com.nexters.teamvs.naenio.ui.feed.detail

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DetailType {
    Default, Random,
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : BaseViewModel() {
    private val _postItem = MutableStateFlow<Post?>(null)
    val postItem = _postItem.asStateFlow()

    fun getPostDetail(id: Int) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _postItem.value = feedRepository.getPostDetail(id = id)
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun getRandomPost() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _postItem.value = feedRepository.getRandomPosts()
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    var voteLock = false
    fun vote(postId: Int, choiceId: Int) {
        if (voteLock) return
        voteLock = true
        viewModelScope.launch {
            try {
                feedRepository.vote(postId, choiceId)
                val post = postItem.value!!
                val alreadyIsVote = post.isVotedForPost()

                post.copy(
                    choices = post.choices.map {
                        if (it.id == choiceId) {
                            it.copy(
                                isVoted = true,
                                voteCount = if (alreadyIsVote) it.voteCount + 1 else it.voteCount
                            )
                        } else {
                            it.copy(
                                isVoted = false,
                                voteCount = if (alreadyIsVote) it.voteCount - 1 else it.voteCount
                            )
                        }
                    }
                ).also {
                    _postItem.value = it
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                voteLock = false
            }

        }
    }
}