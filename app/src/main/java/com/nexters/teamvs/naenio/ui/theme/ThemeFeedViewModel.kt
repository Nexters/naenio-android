package com.nexters.teamvs.naenio.ui.theme

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

@HiltViewModel
class ThemeFeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : BaseViewModel() {

    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts = _posts.asStateFlow()

    fun getThemePosts(type: ThemeType) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                _posts.value = feedRepository.getThemePosts(
                    theme = type.name
                )
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    private var voteLock = false
    fun vote(
        postId: Int,
        choiceId: Int,
    ) {
        if (voteLock) return
        voteLock = true

        val currentPosts = posts.value ?: emptyList()

        /**
         * 같은 선택지에 또 투표를 할 경우, API 요청 막기.
         */
        val post = currentPosts.find { it.id == postId }
        val choice = post?.choices?.find { it.id == choiceId }
        if (post?.isAlreadyVote() == true && choice?.isVoted == true) {
            voteLock = false
            return
        }

        viewModelScope.launch {
            try {
                feedRepository.vote(
                    postId = postId,
                    choiceId = choiceId,
                )

                currentPosts.map { post ->
                    if (post.id == postId) {
                        val isVotedForPost = post.isAlreadyVote()
                        post.copy(
                            choices = post.choices.map { choice ->
                                if (choice.id == choiceId) {
                                    choice.copy(
                                        isVoted = true,
                                        voteCount = choice.voteCount + 1
                                    )
                                } else choice.copy(
                                    isVoted = false,
                                    voteCount = if (isVotedForPost) choice.voteCount - 1 else choice.voteCount
                                )
                            },
                        )
                    } else post
                }.also {
                    _posts.value = it
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                voteLock = false
            }
        }
    }

//    val currentTheme = MutableStateFlow().asStateFlow()

//    fun setType(type: String) {
//        Log.d("#### setType", type)
//        if (type.contains("theme")) {
//            setThemeItem(type, "theme=")
//            getThemePosts(_themeItem.value.type)
//        } else if (type.contains("random")) {
//            setThemeItem(type, "feedDetail=random=")
//            getRandomPost()
//        } else if (type.contains("feedDetail")) {
//            val id = type.replace("feedDetail=", "").toInt()
//            getPostDetail(id)
//        }
//    }

//    private fun setThemeItem(type: String, replaceStr: String) {
//        Log.d("### setThemeItem", "${type} // ${replaceStr}")
//        _themeItem.value = ThemeItem.themeList[type.replace(replaceStr, "").toInt() - 1]
//        Log.d("### setThemeItem", _themeItem.value.type)
//    }

}