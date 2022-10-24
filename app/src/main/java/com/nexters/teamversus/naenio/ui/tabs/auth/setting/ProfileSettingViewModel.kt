package com.nexters.teamversus.naenio.ui.tabs.auth.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nexters.teamversus.naenio.base.BaseViewModel
import com.nexters.teamversus.naenio.domain.repository.UserRepository
import com.nexters.teamversus.naenio.ui.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val uiState = MutableSharedFlow<UiState>()
    val user = userRepository.getUserFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun setProfileInfo(nickname: String, profileImageIndex: Int) {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                val user = userRepository.getMyProfile(viewModelScope)
                Log.d("### user" , "$user")

                if (nickname != user.nickname) {
                    val isExist = userRepository.isExistNickname(nickname)
                    if (isExist) throw AlreadyIsExistNickNameException()
                    userRepository.setNickname(nickname)
                }

                userRepository.setProfileImage(profileImageIndex)
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                Log.e(className, e.stackTraceToString())
                uiState.emit(UiState.Error(e))
            }
        }
    }

}

class AlreadyIsExistNickNameException : Exception()