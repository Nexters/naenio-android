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
        @Query("lastPostId") lastPostId: Int?,
        @Query("sortType") sortType: String?
    ): FeedResponse

    @GET("/app/posts")
    suspend fun getThemePosts(
        @Query("theme") theme: String
    ): FeedResponse

    @GET("/app/posts-random")
    suspend fun getRandomPost(): PostResponse

    @GET("/app/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Int,
    ): PostResponse

    @POST("/app/votes")
    suspend fun vote(@Body voteRequest: VoteRequest): VoteResponse
}