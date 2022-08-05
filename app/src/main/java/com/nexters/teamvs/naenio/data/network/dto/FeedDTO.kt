package com.nexters.teamvs.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class WritePostRequest(
    val title: String,
    val content: String,
    val categoryId: Int,
    val choices: List<String> = emptyList()
)

@Keep
@Serializable
data class FeedRequest(
    val size: Int,
    val lastPostId: Int //첫 조회, 새로고침의 경우 lastPostId를 비워서 보내면 됩니다
)

@Keep
@Serializable
data class FeedResponse(
    val postResponses: List<PostResponse>
)

@Keep
@Serializable
data class PostResponse(
    val author: AuthorResponse,
    val choices: List<ChoiceResponse>,
    val commentCount: Int,
    val content: String,
    val id: Int,
    val title: String
)

@Keep
@Serializable
data class ChoiceResponse(
    val id: Int,
    val isVoted: Boolean,
    val name: String,
    val sequence: Int,
    val voteCount: Int
)

@Keep
@Serializable
data class AuthorResponse(
    val id: Int,
    val nickname: String,
    val profileImageIndex: Int
)