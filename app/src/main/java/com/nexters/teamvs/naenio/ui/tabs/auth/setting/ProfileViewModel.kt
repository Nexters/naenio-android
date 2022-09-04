package com.nexters.teamvs.naenio.ui.tabs.auth.setting

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.ui.model.UiState
import com.nexters.teamvs.naenio.utils.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO ProfileViewModel이 2개임,,?
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : BaseViewModel() {

    val uiState = MutableSharedFlow<UiState>()
    private val userState = userPreferencesRepository.userPrefFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    fun setProfileInfo(nickname: String, profileImageIndex: Int) {
        viewModelScope.launch {
            try {
                uiState.emit(UiState.Loading)
                val isExist = userRepository.isExistNickname(nickname)
                if (isExist) throw AlreadyIsExistNickNameException()

                val nicknameDef = async { userRepository.setNickname(nickname) }
                val profileImageDef = async { userRepository.setProfileImage(profileImageIndex) }
                awaitAll(nicknameDef, profileImageDef)
                saveUserInfo(nickname, profileImageIndex)
                uiState.emit(UiState.Success)
            } catch (e: Exception) {
                Log.e(className, e.stackTraceToString())
                uiState.emit(UiState.Error(e))
            }
        }
    }

    private suspend fun saveUserInfo(nickname: String, profileImageIndex: Int) {
        userPreferencesRepository.updateUserPreferences(
            userState.value!!.copy(
                nickname = nickname,
                profileImageIndex = profileImageIndex
            )
        )
    }

    fun logout() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.logOut()
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.signOut()
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
                GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
            }
        }
    }

}

class AlreadyIsExistNickNameException : Exception()