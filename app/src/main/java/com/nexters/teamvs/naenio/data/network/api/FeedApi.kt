package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface FeedApi {
    @POST("/app/posts")
    suspend fun writePost(
        @Body writePostRequest: WritePostRequest
    ): PostResponse

    @GET("/app/feed")
    suspend fun getFeedPosts(
        @Body feedRequest: FeedRequest
    ): FeedResponse
}