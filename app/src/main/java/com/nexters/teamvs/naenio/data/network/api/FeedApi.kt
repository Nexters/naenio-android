package com.nexters.teamvs.naenio.data.network.api

import com.google.gson.JsonElement
import com.nexters.teamvs.naenio.data.network.dto.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface FeedApi {

    @POST("/app/posts")
    suspend fun createPost(
        @Body createRequest: CreateRequest
    ): CreateResponse

    @DELETE("/app/posts/{id}")
    suspend fun deletePost(
        @Path("id") postId: Int
    )

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

    @POST("/app/reports")
    suspend fun report(
        @Body reportRequest: ReportRequest
    ): Response<Unit>
}