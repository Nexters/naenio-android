package com.nexters.teamvs.naenio.data.network

import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val authDataStore: AuthDataStore = AuthDataStore
) : Interceptor {

    //TODO 만료토큰 갱신
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = authDataStore.authToken

        val tokenRequest = if (authToken.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer test_member_3") //test_member_3
                .addHeader("Accept", "application/json;charset=utf-8")
                .build()
        } else {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer test_member_3") //test_member_3
                .addHeader("Accept", "application/json;charset=utf-8")
                .build()
        }

        return chain.proceed(tokenRequest)
    }
}