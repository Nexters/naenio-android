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
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.BaseViewModel
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.NaenioApp
import com.nexters.teamvs.naenio.data.network.dto.AuthType
import com.nexters.teamvs.naenio.domain.model.Profile
import com.nexters.teamvs.naenio.domain.repository.UserRepository
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.extensions.requireActivity
import com.nexters.teamvs.naenio.theme.NaenioTypography
import com.nexters.teamvs.naenio.ui.model.User
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import com.nexters.teamvs.naenio.utils.fromJson
import com.nexters.teamvs.naenio.utils.loginWithKakao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginDestination {
    object ProfileSettings : LoginDestination()
    object Main : LoginDestination()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _loginDetailText = MutableStateFlow<String?>(null)
    val loginDetailText = _loginDetailText.asStateFlow()

    private val _isLoginSuccess = MutableStateFlow<Boolean?>(null)
    val isLoginSuccess = _isLoginSuccess.asStateFlow()

    val navigationEvent = MutableSharedFlow<LoginDestination>(extraBufferCapacity = 1)

    init {
        _isLoginSuccess.value = false
    }

    private suspend fun login(socialLoginToken: String, authType: AuthType) {
        userRepository.login(socialLoginToken, authType)
    }

    fun loginKakao(context: Context) {
        viewModelScope.launch {
            try {
//                GlobalUiEvent.showLoading() TODO 로고랑 로딩바랑 아이콘이 동일해서 어색해보이는 이슈
                val token = loginWithKakao(context)
                Log.d(className, "$token")
                login(token.accessToken, AuthType.KAKAO)
//                checkProfileInfo()
                navigationEvent.tryEmit(LoginDestination.ProfileSettings)
            } catch (e: Exception) {
                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                    Log.d(className, "사용자가 명시적으로 취소")
                } else {
                    GlobalUiEvent.showToast(e.errorMessage())
                }
            } finally {
                _isLoginSuccess.value = true
                GlobalUiEvent.hideLoading()
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
        viewModelScope.launch {
            try {
                GlobalUiEvent.showLoading()
                val account = completedTask.result
                Log.d(className, "[HandleGoogleSignInResult] success data :: " + account.email)
                Log.d(
                    className,
                    "[HandleGoogleSignInResult] success id_token :: ${account.idToken}"
                )
                login(account.idToken!!, AuthType.GOOGLE)
                checkProfileInfo()
            } catch (e: ApiException) {
                GlobalUiEvent.showToast(e.errorMessage())
                Log.e(
                    className,
                    "[HandleGoogleSignInResult] failed code :: " + e.statusCode.toString()
                )
            } catch (e: Exception) {
                GlobalUiEvent.showToast(e.errorMessage())
                e.printStackTrace()
            } finally {
                _isLoginSuccess.value = true
                GlobalUiEvent.hideLoading()
            }
        }
    }

    private fun checkProfileInfo() {
        try {
            val user = AuthDataStore.userJson.fromJson<User>()
            if (user == null || user.nickname.isNullOrEmpty()) {
                Log.d("###checkProfileInfo", "NULL???")
                navigationEvent.tryEmit(LoginDestination.ProfileSettings)
            } else {
                Log.d("###checkProfileInfo", "NOT NULL???")
                navigationEvent.tryEmit(LoginDestination.Main)
            }
        } catch (e: Exception) {
            Log.e(className, e.stackTraceToString())
            navigationEvent.tryEmit(LoginDestination.ProfileSettings)
        }
    }

    fun setLoginDetailType(type: String) {
        try {
            val input: java.io.InputStream
            if (type == LoginDetailType.SERVICE) {
                input = NaenioApp.context.resources.openRawResource(R.raw.service)
            } else {
                input = NaenioApp.context.resources.openRawResource(R.raw.privacy)
            }
            val stream = java.io.InputStreamReader(input, "utf-8")
            val buffer = java.io.BufferedReader(stream)
            var sb = java.lang.StringBuilder("")
            var read : String?
            do {
                read = buffer.readLine()
                sb.append("$read\n")
            } while (read != null)
            input.close()
            _loginDetailText.value = sb.toString()
        } catch (e: Exception) {
            Log.e(className, e.stackTraceToString())
            _loginDetailText.value = ""
        }
    }
}

