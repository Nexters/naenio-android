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

    @POST("/app/comment-likes")
    suspend fun likeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): CommentResponse

    @DELETE("/app/comment-likes")
    suspend fun unlikeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): CommentResponse

    @POST("/app/comments")
    suspend fun writeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): CommentResponse

    @GET("/app/feed")
    suspend fun getFeedPosts(
        @Body feedRequest: FeedRequest
    ): FeedResponse
}