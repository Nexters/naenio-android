package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.*

interface NaenioApi {

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

    @POST("/app/posts")
    suspend fun writePost(
        @Body writePostRequest: WritePostRequest
    ): PostResponse
}