package com.nexters.teamversus.naenio.ui.tabs.auth

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
import com.nexters.teamversus.naenio.BuildConfig
import com.nexters.teamversus.naenio.base.BaseViewModel
import com.nexters.teamversus.naenio.data.UserRepository
import com.nexters.teamversus.naenio.data.network.dto.AuthType
import com.nexters.teamversus.naenio.utils.loginWithKakao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {

    private fun login(socialLoginToken: String, authType: AuthType) {
        viewModelScope.launch {
            userRepository.login(socialLoginToken, authType)
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
            if(account.idToken.isNullOrEmpty()) return
            login(account.idToken!!, AuthType.GOOGLE)
        } catch (e: ApiException) {
            Log.e(className, "[HandleGoogleSignInResult] failed code :: " + e.statusCode.toString())
        }
    }

    fun isExistNickname(nickname: String) {
        viewModelScope.launch {
            try {
                val isExist = userRepository.isExistNickname(nickname)
                Log.d(className, "$isExist")
            } catch (e: Exception) {
                Log.e(className, e.stackTraceToString())
            }
        }
    }

    fun setNickname(nickname: String) {
        viewModelScope.launch {
            try {
                val isExist = userRepository.setNickname(nickname)
                Log.d(className, "$isExist")
            } catch (e: Exception) {
                Log.e(className, e.stackTraceToString())
            }
        }
    }

}