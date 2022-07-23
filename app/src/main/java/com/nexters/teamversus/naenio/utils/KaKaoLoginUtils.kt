package com.nexters.teamversus.naenio.utils

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 카카오톡 설치 여부에 따라서 설치 되어있으면 카카오톡 로그인을 시도한다.
 * 미설치 시 카카오 계정 로그인을 시도한다.
 *
 * 카카오톡 로그인에 실패하면 사용자가 의도적으로 로그인 취소한 경우를 제외하고는 카카오 계정 로그인을 서브로 실행한다.
 */
suspend fun loginWithKakao(context: Context): OAuthToken {
    return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        try {
            loginWithKakaoTalk(context)
        } catch (error: Throwable) {
            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
            // 그냥 에러를 올린다.
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error

            // 그렇지 않다면, 카카오 계정 로그인을 시도한다.
            loginWithKakaoAccount(context)
        }
    } else {
        loginWithKakaoAccount(context)
    }
}

/**
 * 카카오톡으로 로그인 시도
 */
private suspend fun loginWithKakaoTalk(context: Context): OAuthToken {
    return suspendCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (token != null) {
                continuation.resume(token)
            } else {
                continuation.resumeWithException(RuntimeException("### kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
            }
        }
    }
}

/**
 * 카카오 계정으로 로그인 시도
 */
private suspend fun loginWithKakaoAccount(context: Context): OAuthToken {
    return suspendCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (token != null) {
                continuation.resume(token)
            } else {
                continuation.resumeWithException(RuntimeException("### kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
            }
        }
    }
}

private fun getHashKey(context: Context) {
    val keyHash = Utility.getKeyHash(context)
    Log.i("++ KeyHash" , keyHash)
}
