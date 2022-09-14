package com.nexters.teamvs.naenio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.Route
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        var deepLinkPostId: Int? = null //흑마법,,
    }

    var isReady: Boolean = false
    val startDestination = MutableStateFlow(AuthScreen.Login.route)

    val navigateEvent = MutableSharedFlow<String>()

    init {
        onBoarding()
    }

    private suspend fun findNavDestination(): String {
        val jwt = AuthDataStore.authToken.also {
            Log.d("### AuthToken", it)
        }

        return if (jwt.isEmpty()) {
            AuthScreen.Login.route
        } else {
            val user = userRepository.getMyProfile(viewModelScope)
            if (user.nickname.isNullOrEmpty()) {
                AuthScreen.ProfileSetting.route
            } else {
                Graph.MAIN
            }
        }
    }

    private fun onBoarding() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                startDestination.emit(findNavDestination())
            } catch (e: Exception) {
                e.errorMessage()
            } finally {
                isReady = true
            }
        }
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

    fun handleDeepLink(postId: Int?) {
        viewModelScope.launch {
            startDestination.emit(findNavDestination())
            postId?.let {
                navigateEvent.emit("FeedDeepLinkDetail/${postId}")
                deepLinkPostId = it
            }
        }
    }
}