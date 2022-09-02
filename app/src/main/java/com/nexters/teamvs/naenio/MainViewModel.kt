package com.nexters.teamvs.naenio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import com.nexters.teamvs.naenio.utils.datastore.UserPref
import com.nexters.teamvs.naenio.utils.datastore.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var isReady: Boolean = false
    val startDestination = MutableStateFlow(AuthScreen.Login.route)

    private val userPreferencesFlow = userPreferencesRepository.userPrefFlow
    private var user: StateFlow<UserPref?> = userPreferencesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    init {
        onBoarding()
    }

    private fun onBoarding() {
        viewModelScope.launch(Dispatchers.Main) {
            val jwt = AuthDataStore.authToken.also {
                Log.d("### AuthToken", it)
            }

            if (jwt.isEmpty()) {
                startDestination.emit(AuthScreen.Login.route)
            } else {
                if (user.value?.nickname.isNullOrBlank()) {
                    startDestination.emit(AuthScreen.ProfileSetting.route)
                } else {
                    startDestination.emit(AuthScreen.Login.route)
                }
            }

            isReady = true
        }
    }
}