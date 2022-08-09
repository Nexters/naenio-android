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
data class PostResponse(
    val author: AuthorResponse,
    val choices: List<ChoiceResponse>,
    val commentCount: Int = 0,
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
    val nickname: String?,
    val profileImageIndex: Int?
)