package com.nexters.teamversus.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val authToken: String,
    val authServiceType: String = "KAKAO"
)

@Serializable
data class LoginResponse(
    val token: String
)