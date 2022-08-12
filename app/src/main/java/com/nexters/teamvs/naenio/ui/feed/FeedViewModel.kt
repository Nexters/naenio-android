package com.nexters.teamvs.naenio.ui.feed

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
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

    val uiState = MutableSharedFlow<UiState>()

    init {
        getFeedPosts()
    }

    fun getFeedPosts() {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                _posts.value = feedRepository.getFeedPosts(
                    pageSize = 10,
                    lastPostId = null
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