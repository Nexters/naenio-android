package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : BaseViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
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

    val uiState = MutableSharedFlow<UiState>()

    init {
    }

    fun setType(type : String) {
//        if (type == "feed") {
//            _feedTabItems.value = FeedTabItem.feedButtonList
//        }
        if (type.contains("feedDetail")) {
            val itemIndex = type.replace("feedDetail=","").toInt()
            _postItem.value = _posts.value[itemIndex]
        }
        if (type.contains("theme") ){
            setThemeItem(type, "theme=")
            getThemePosts(_themeItem.value.type)
        }
        if (type.contains("random")) {
            setThemeItem(type, "random=")
            getRandomPost()
        }
    }

    private fun setThemeItem(type: String, replaceStr:String) {
        _themeItem.value = ThemeItem.themeList[type.replace(replaceStr,"").toInt()-1]
    }

    private fun getThemePosts(type : String) {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                if (type == "ALL") {

                }
                _posts.value = feedRepository.getThemePosts(
                    theme = type
                )
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.emit(UiState.Error(e))
            } finally {
//                uiState.emit(UiState.Success)
            }
        }
    }

    fun getRandomPost() {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                _postItem.value = feedRepository.getRandomPosts()
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.emit(UiState.Error(e))
            } finally {
//                uiState.emit(UiState.Success)
            }
        }
    }

    fun getFeedPosts(sortType : String) {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                val sortTypes = if (sortType == "ALL") {
                    null
                } else {
                    sortType
                }
                _posts.value = feedRepository.getFeedPosts(
                    pageSize = 10,
                    lastPostId = null,
                    sortType = sortTypes
                )
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.emit(UiState.Error(e))
            } finally {
//                uiState.emit(UiState.Success)
            }
        }
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
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
              voteLock = false
            }
        }
    }

}