package com.nexters.teamversus.naenio.data.network.dto

import androidx.annotation.Keep
import androidx.compose.runtime.saveable.autoSaver
import com.nexters.teamversus.naenio.domain.model.Post
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FeedResponse(
    val posts: List<PostResponse>
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
    val nickname: String = "",
    val profileImageIndex: Int = 0
)

@Keep
@Serializable
data class VoteRequest(
    val postId: Int,
    val choiceId: Int
)

@Keep
@Serializable
data class VoteResponse(
    val id: Int,
    val postId: Int,
    val choiceId: Int,
    val memberId: Int,
//    val createDateTime: String,
//    val lastModifiedDateTime: String,
)