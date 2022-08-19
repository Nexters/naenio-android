package com.nexters.teamvs.naenio.ui.tabs.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.nexters.teamvs.naenio.BuildConfig
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.data.network.dto.AuthType
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.graphs.Route
import com.nexters.teamvs.naenio.ui.model.User
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import com.nexters.teamvs.naenio.utils.fromJson
import com.nexters.teamvs.naenio.utils.loginWithKakao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginDestination {
    object ProfileSettings: LoginDestination()
    object Main: LoginDestination()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val navigationEvent = MutableSharedFlow<LoginDestination>(extraBufferCapacity = 1)

    private fun login(socialLoginToken: String, authType: AuthType) {
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                userRepository.login(socialLoginToken, authType)
                checkProfileInfo()
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
            } finally {
                GlobalUiEvent.hideLoading()
            }
        }
    }

    fun loginKakao(context: Context) {
        viewModelScope.launch {
            try {
                val token = loginWithKakao(context)
                Log.d(className, "$token")
                login(token.accessToken, AuthType.KAKAO)
            } catch (e: Exception) {
                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                    Log.d(className, "사용자가 명시적으로 취소")
                } else {
                    Log.e(className, "인증 에러 발생", e)
                }
            }
        }
    }

    fun getGoogleLoginAuth(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.result
            Log.d(className, "[HandleGoogleSignInResult] success data :: " + account.email)
            Log.d(className, "[HandleGoogleSignInResult] success id_token :: " + account.idToken)
            if (account.idToken.isNullOrEmpty()) return
            login(account.idToken!!, AuthType.GOOGLE)
        } catch (e: ApiException) {
            Log.e(className, "[HandleGoogleSignInResult] failed code :: " + e.statusCode.toString())
        }
    }

    fun checkProfileInfo() {
        val user = AuthDataStore.userJson.fromJson<User>()
        if (user == null || user.nickname.isNullOrEmpty()) {
            navigationEvent.tryEmit(LoginDestination.ProfileSettings)
        } else {
            navigationEvent.tryEmit(LoginDestination.Main)
        }
    }
}
