package com.nexters.teamvs.naenio.ui.tabs.auth.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.ui.model.UiState
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
                val isExist = userRepository.isExistNickname(nickname)
                if (isExist) throw AlreadyIsExistNickNameException()

                Log.d("### user" , "${user.value}")

                val nicknameDef = async { userRepository.setNickname(nickname) }
                val profileImageDef = async { userRepository.setProfileImage(profileImageIndex) }

                awaitAll(nicknameDef, profileImageDef)
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                Log.e(className, e.stackTraceToString())
                uiState.emit(UiState.Error(e))
            }
        }
    }

}

class AlreadyIsExistNickNameException : Exception()