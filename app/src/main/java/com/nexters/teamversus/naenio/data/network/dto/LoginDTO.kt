package com.nexters.teamversus.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LoginRequest(
    val authToken: String,
    val authServiceType: AuthType
)

@Keep
@Serializable
enum class AuthType {
    KAKAO, GOOGLE
}

@Keep
@Serializable
data class LoginResponse(
    val token: String
)

@Keep
@Serializable
data class IsExistNicknameResponse(
    val exist: Boolean
)

@Keep
@Serializable
data class NicknameRequest(
    val nickname: String
)

@Keep
@Serializable
data class NicknameResponse(
    val nickname: String
)

@Keep
@Serializable
data class MyProfileResponse(
    val id: Int,
    val nickname: String = "",
    val authServiceType: String,
    val profileImageIndex: Int = 0
)

@Keep
@Serializable
data class ProfileImageRequest(
    val profileImageIndex: Int,
)

@Keep
@Serializable
data class ProfileImageResponse(
    val profileImageIndex: Int,
)

@Keep
@Serializable
data class BlockRequest(
    val targetMemberId: Int,
)