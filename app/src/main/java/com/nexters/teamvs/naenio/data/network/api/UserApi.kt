package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.*

interface UserApi {

    @POST("/app/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("/app/members/exist")
    suspend fun isExistNickname(
        @Query("nickname") nickname: String
    ): IsExistNicknameResponse

    @PUT("/app/members/nickname")
    suspend fun setNickname(
        @Body nicknameRequest: NicknameRequest
    ): NicknameResponse

    @GET("/app/members/me")
    suspend fun getMyProfile(): MyProfileResponse

    @DELETE("/app/members/me")
    suspend fun deleteProfile() : Object

    @PUT("/app/members/profile-image")
    suspend fun setProfileImage(
        @Body profileImageRequest: ProfileImageRequest
    ): ProfileImageResponse
}