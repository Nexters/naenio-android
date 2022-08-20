package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.*

interface CommentApi {

    @GET("/app/posts/{id}/comments")
    suspend fun getComments(
        @Path("id") id: Int,
        @Query("size") size: Int,
        @Query("lastCommentId") lastCommentId: Int?,
    ): CommentResponse

    @GET("/app/comments/{id}/comment-replies")
    suspend fun getCommentReplies(
        @Path("id") id: Int,
        @Query("size") size: Int,
        @Query("lastCommentId") lastCommentId: Int?,
    ): ReplyResponse

    @POST("/app/comment-likes")
    suspend fun likeComment (
        @Body likeCommentRequest: LikeCommentRequest
    ): LikeCommentResponse

    @DELETE("/app/comment-likes/{id}")
    suspend fun unlikeComment (
        @Path("id") id: Int,
    )

    @DELETE("/app/comments/{id}")
    suspend fun deleteComment (
        @Path("id") id: Int,
    )

    @POST("/app/comments")
    suspend fun writeComment (
        @Body writeCommentRequest: WriteCommentRequest
    ): WriteCommentResponse

    @GET("/app/commments/me")
    suspend fun getMyComment (
        @Query("size") size : Int
    ) : MyCommentResponse
}