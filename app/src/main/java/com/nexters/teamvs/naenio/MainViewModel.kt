package com.nexters.teamvs.naenio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isReady: Boolean = false
    val startDestination = MutableStateFlow(AuthScreen.Login.route)

    init {
        onBoarding()
    }

    private fun onBoarding() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val jwt = AuthDataStore.authToken.also {
                    Log.d("### AuthToken", it)
                }

                if (jwt.isEmpty()) {
                    startDestination.emit(AuthScreen.Login.route)
                } else {
                    val user = userRepository.getMyProfile(viewModelScope)
                    if (user.nickname.isNullOrEmpty()) {
                        startDestination.emit(AuthScreen.ProfileSetting.route)
                    } else {
                        startDestination.emit(Graph.MAIN)
                    }
                }
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                isReady = true
            }
        }
    }
}