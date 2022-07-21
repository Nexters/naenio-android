package com.nexters.teamversus.naenio.data.network.api

import com.nexters.teamversus.naenio.data.network.dto.LoginRequest
import com.nexters.teamversus.naenio.data.network.dto.LoginResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NaenioApi {
    @GET("posts/1")
    suspend fun getData(): JsonElement

    @POST("app/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}