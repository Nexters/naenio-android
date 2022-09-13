package com.nexters.teamvs.naenio.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.model.Choice
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CreateEvent {
    object Loading : CreateEvent()
    data class Success(val post: Post) : CreateEvent()
    data class Error(val exception: Exception) : CreateEvent()
}

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val feedRepository: FeedRepository
): ViewModel() {

    val createEvent = MutableSharedFlow<CreateEvent>()

    fun createPost(
        title: String,
        content: String,
        choices: Array<String>
    ) {
        viewModelScope.launch {
            try {
                createEvent.emit(CreateEvent.Loading)
                feedRepository.createPost(
                    title = title,
                    content = content,
                    choices = choices
                ).let {
                    createEvent.emit(CreateEvent.Success(it))
                }
            } catch (e: Exception) {
                createEvent.emit(CreateEvent.Error(e))
                GlobalUiEvent.showToast(e.errorMessage())
            }
        }
    }
}