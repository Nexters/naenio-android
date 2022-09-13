package com.nexters.teamvs.naenio.ui.feed.detail

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.data.network.dto.ReportRequest
import com.nexters.teamvs.naenio.data.network.dto.ReportType
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.comment.CommentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    userRepository: UserRepository,
) : BaseViewModel() {

    private val _postItem = MutableStateFlow<Post?>(null)
    val postItem = _postItem.asStateFlow()

    val successVote = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    val user = userRepository.getUserFlow()

    init {
        viewModelScope.launch {
            CommentViewModel.dismissCommentDialog.collect { data ->
                val post = postItem.value
                val result = if (post?.id == data.postId) post.copy(commentCount = data.commentCount) else postItem.value
                _postItem.value = result
            }
        }
    }

    fun getRandomPost() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _postItem.value = feedRepository.getRandomPosts()
            } catch (e: SerializationException) {
                e.printStackTrace()
                GlobalUiEvent.showToast("랜덤 컨텐츠가 없습니다 ㅜㅜ. 재시도 해주세요!")
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    suspend fun getPostDetail(id: Int): Post {
        return feedRepository.getPostDetail(id = id).also {
            _postItem.value = it
        }
    }

    var voteLock = false
    fun vote(postId: Int, choiceId: Int) {
        if (voteLock) return
        voteLock = true
        viewModelScope.launch {
            try {
                val post = postItem.value!!

                /**
                 * 같은 선택지에 또 투표를 할 경우, API 요청 막기.
                 */
                val choice = post.choices.find { it.id == choiceId }
                if (post.isAlreadyVote && choice?.isVoted == true) {
                    voteLock = false
                    return@launch
                }

                feedRepository.vote(postId, choiceId)
                val alreadyIsVote = post.isAlreadyVote

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
                    successVote.emit(true)
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                voteLock = false
            }

        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                feedRepository.deletePost(postId)
                _postItem.value = null
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }

    fun report(targetMemberId: Int, resourceType: ReportType) {
        viewModelScope.launch {
            try {
                feedRepository.report(
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
}