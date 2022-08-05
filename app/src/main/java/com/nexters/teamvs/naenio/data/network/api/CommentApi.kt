package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.CommentResponse
import com.nexters.teamvs.naenio.data.network.dto.LikeCommentRequest
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentRequest
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface CommentApi {
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
        @Body writeCommentRequest: WriteCommentRequest
    ): WriteCommentResponse
}