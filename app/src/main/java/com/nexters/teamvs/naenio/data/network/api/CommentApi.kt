package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.*

interface CommentApi {

    @GET("/app/comments/{id}/comments")
    suspend fun getComments(
        @Path("id") id: Int,
        @Query("size") size: Int,
        @Query("lastCommentId") lastCommentId: Int?,
    ): CommentResponse

    @GET("/app/comments/{id}/comment-replies")
    suspend fun getCommentReplies(
        @Path("id") id: Int,
        @Query("size") size: Int,
        @Query("lastCommentId") lastCommentId: Int,
    ): ReplyResponse

    @POST("/app/comment-likes")
    suspend fun likeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): LikeCommentResponse

    @DELETE("/app/comment-likes")
    suspend fun unlikeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): LikeCommentResponse

    @POST("/app/comments")
    suspend fun writeComment (
        @Body writeCommentRequest: WriteCommentRequest
    ): WriteCommentResponse
}