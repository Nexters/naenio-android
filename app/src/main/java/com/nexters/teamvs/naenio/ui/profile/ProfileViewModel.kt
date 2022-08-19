package com.nexters.teamvs.naenio.ui.profile

import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.data.network.dto.MyProfileResponse
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.domain.model.Profile
import com.nexters.teamvs.naenio.domain.repository.FeedRepository
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import com.nexters.teamvs.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _myProfile = MutableStateFlow<Profile?>(null)
    val myProfile = _myProfile.asStateFlow()

    val uiState = MutableSharedFlow<UiState>()

    init {
        getMyProfile()
    }

    fun getMyProfile() {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                _myProfile.value = userRepository.getMyProfile()
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.emit(UiState.Error(e))
            } finally {
                // uiState.emit(UiState.Success)

            }
        }

    }
}