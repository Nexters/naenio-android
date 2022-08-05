package com.nexters.teamvs.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LikeCommentRequest(val commentId: String)

@Serializable
@Keep
data class CommentResponse(
    val commentId: Int,
    val createdDateTime: String,
    val id: Int,
    val lastModifiedDateTime: String,
    val memberId: Int
)

@Serializable
@Keep
data class WriteCommentRequest(
    val parentId: String,
    val parentType: String = "POST", //TODO 대댓글 타입은?
    val content: String,
)


@Serializable
@Keep
data class WriteCommentResponse(
    val content: String,
    val createdDateTime: String,
    val id: Int,
    val lastModifiedDateTime: String,
    val memberId: Int,
    val parentId: Int,
    val parentType: String
)
