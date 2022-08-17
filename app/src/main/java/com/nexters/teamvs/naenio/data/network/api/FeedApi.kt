package com.nexters.teamvs.naenio.data.network.api

import com.nexters.teamvs.naenio.data.network.dto.*
import retrofit2.http.*

interface FeedApi {

    @POST("/app/posts")
    suspend fun createPost(
        @Body createRequest: CreateRequest
    ): CreateResponse

    @GET("/app/feed")
    suspend fun getFeedPosts(
        @Query("size") size: Int,
        @Query("lastPostId") lastPostId: Int?
    ): FeedResponse
}