package com.nexters.teamversus.naenio.data.network

import com.nexters.teamversus.naenio.utils.datastore.AuthDataStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val authDataStore: AuthDataStore = AuthDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = authDataStore.authToken

        val tokenRequest = if (authToken.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $authToken") //test_member_3
                .build()
        } else {
            chain.request().newBuilder()
                .build()
        }

        return chain.proceed(tokenRequest)
    }
}