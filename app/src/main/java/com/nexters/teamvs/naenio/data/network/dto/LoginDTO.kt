package com.nexters.teamvs.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val authToken: String,
    val authServiceType: AuthType
)

enum class AuthType {
    KAKAO, GOOGLE
}

@Serializable
data class LoginResponse(
    val token: String
)

@Serializable
data class IsExistNicknameResponse(
    val exist: Boolean
)

@Serializable
data class NicknameRequest(
    val nickname: String
)

@Serializable
data class NicknameResponse(
    val nickname: String
)

@Serializable
data class ProfileImageRequest(
    val profileImageIndex: Int,
)

@Serializable
data class ProfileImageResponse(
    val profileImageIndex: Int,
)