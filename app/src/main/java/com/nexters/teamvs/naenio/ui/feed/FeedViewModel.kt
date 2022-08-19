package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
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

    private val _feedButtonItem = MutableStateFlow<List<FeedButtonItem>>(emptyList())
    val feedButtonItem = _feedButtonItem.asStateFlow()

    val uiState = MutableSharedFlow<UiState>()

    init {
    }

    fun setType(type: String) {
        if (type == "feed") {
            _feedButtonItem.value = FeedButtonItem.feedButtonList
        }
        if (type.contains("feedDetail")) {
            val id = type.replace("feedDetail=", "").toInt()
            getPostDetail(id)
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

    private fun getPostDetail(id: Int) {
        viewModelScope.launch {
            try {
                _postItem.value = feedRepository.getPostDetail(id = id)
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.emit(UiState.Error(e))
            } finally {
            }
        }
    }

    private fun setThemeItem(type: String, replaceStr: String) {
        _themeItem.value = ThemeItem.themeList[type.replace(replaceStr, "").toInt() - 1]
    }

    private fun getThemePosts(type: String) {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
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

    fun getFeedPosts(sortType: String) {
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

}