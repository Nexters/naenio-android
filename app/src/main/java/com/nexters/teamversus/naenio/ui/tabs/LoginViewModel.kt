package com.nexters.teamversus.naenio.ui.tabs

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
import com.nexters.teamversus.naenio.base.BaseViewModel
import com.nexters.teamversus.naenio.data.network.ApiProvider
import com.nexters.teamversus.naenio.data.network.api.NaenioApi
import com.nexters.teamversus.naenio.data.network.dto.LoginRequest
import com.nexters.teamversus.naenio.utils.datastore.AuthDataStore
import com.nexters.teamversus.naenio.utils.loginWithKakao
import kotlinx.coroutines.launch

class LoginViewModel(
    private val naenioApi: NaenioApi = ApiProvider.retrofit.create(NaenioApi::class.java)
): BaseViewModel() {

    private fun login(token: String) {
        viewModelScope.launch {
            naenioApi.login(LoginRequest(token)).also {
                val authToken = it.token
                if (authToken.isNotEmpty()) {
                    AuthDataStore.authToken = authToken
                }
                Log.d(className, AuthDataStore.authToken)

                Log.d(className, it.toString())
            }
        }
    }

    fun loginKakao(context: Context) {
        viewModelScope.launch {
            try {
                val token = loginWithKakao(context)
                Log.d(className, "$token")
                login(token.accessToken)
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
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.result
            Log.d(className, "[HandleGoogleSignInResult] success data :: " + account.email)
        } catch (e: ApiException) {
            Log.e(className, "[HandleGoogleSignInResult] failed code :: " + e.statusCode.toString())
        }
    }

}