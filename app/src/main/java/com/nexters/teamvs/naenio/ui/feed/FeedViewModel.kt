package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FeedEvent {
    object ScrollToTop : FeedEvent()
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : BaseViewModel() {

    private val _posts = MutableStateFlow<List<Post>?>(null)
    val posts = _posts.asStateFlow()

    private val _themeItem = MutableStateFlow<ThemeItem>(ThemeItem())
    val themeItem = _themeItem.asStateFlow()

    private val _postItem = MutableStateFlow<Post?>(null)
    val postItem = _postItem.asStateFlow()

    private val _feedTabItems = MutableStateFlow(feedRepository.getFeedTabItems())
    val feedTabItems = _feedTabItems.asStateFlow()

    private val _selectedTab = MutableStateFlow<FeedTabItemModel>(feedTabItems.value[0])
    val selectedTab = _selectedTab.asStateFlow()
    fun selectTab(feedTabItemModel: FeedTabItemModel) {
        _selectedTab.value = feedTabItemModel
    }

    val event = MutableSharedFlow<FeedEvent>()
    fun emitEvent(feedEvent: FeedEvent) {
        viewModelScope.launch {
            event.emit(feedEvent)
        }
    }

    init {
        viewModelScope.launch {
            selectedTab.collect {
                try {
                    GlobalUiEvent.showLoading()
                    getFeedPosts(it.type)
                    emitEvent(FeedEvent.ScrollToTop)
                } catch (e: Exception) {
                    GlobalUiEvent.showToast(e.errorMessage())
                } finally {
                    GlobalUiEvent.hideLoading()
                }
            }
        }
    }

    fun setType(type: String) {
//        if (type == "feed") {
//            _feedTabItems.value = FeedTabItem.feedButtonList
//        }
        if (type.contains("feedDetail")) {
            val itemIndex = type.replace("feedDetail=", "").toInt()
//            _postItem.value = _posts.value[itemIndex]
        }
        if (type.contains("theme")) {
            setThemeItem(type, "theme=")
            getThemePosts(_themeItem.value.type)
        }
        if (type.contains("random")) {
            setThemeItem(type, "random=")
            getRandomPost()
        }
    }

    private fun setThemeItem(type: String, replaceStr: String) {
        _themeItem.value = ThemeItem.themeList[type.replace(replaceStr, "").toInt() - 1]
    }

    private fun getPostDetail(id: Int) {
        viewModelScope.launch {
            try {
                _postItem.value = feedRepository.getPostDetail(id = id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    private fun getThemePosts(type: String) {
        viewModelScope.launch {
            try {
                _posts.value = feedRepository.getThemePosts(
                    theme = type
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    fun getRandomPost() {
        viewModelScope.launch {
            try {
                _postItem.value = feedRepository.getRandomPosts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    private suspend fun getFeedPosts(feedTabItemType: FeedTabItemType) {
        _posts.value = feedRepository.getFeedPosts(
            pageSize = 10,
            lastPostId = null,
            sortType = feedTabItemType.text
        )
    }

    var voteLock = false
    fun vote(
        postId: Int,
        choiceId: Int,
    ) {
        if (voteLock) return
        voteLock = true
        viewModelScope.launch {
            try {
                feedRepository.vote(
                    postId = postId,
                    choiceId = choiceId,
                )
                //TODO 간결하게 수정
                val currentPosts = posts.value ?: emptyList()
                currentPosts.map { post ->
                    if (post.id == postId) {
                        post.copy(choices = post.choices.map { choice ->
                            if (choice.id == choiceId) {
                                choice.copy(
                                    isVoted = true,
                                )
                            } else choice.copy(
                                isVoted = false
                            )
                        })
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

}