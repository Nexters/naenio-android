package com.nexters.teamvs.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LikeCommentRequest(val commentId: String)

@Serializable
@Keep
data class LikeCommentResponse(
    val commentId: Int,
    val createdDateTime: String,
    val id: Int,
    val lastModifiedDateTime: String,
    val memberId: Int
)

@Serializable
@Keep
data class CommentResponse(
    val comments: List<PostCommentResponse>,
    val totalCommentCount: Int
)

@Serializable
@Keep
data class PostCommentResponse(
    val author: AuthorResponse,
    val createdDatetime: String,
    val content: String,
    val id: Int,
    val isLiked: Boolean,
    val likeCount: Int,
    val repliesCount: Int
)

@Serializable
@Keep
data class WriteCommentRequest(
    val parentId: String,
    val parentType: CommentParentType, //TODO 대댓글 타입은?
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


@Serializable
@Keep
data class ReplyResponse(
    val commentReplies: List<CommentReply>
)

@Serializable
@Keep
data class CommentReply(
    val author: AuthorResponse,
    val createdDatetime: String,
    val id: Int,
    val isLiked: Boolean,
    val likeCount: Int
)

@Serializable
@Keep
enum class CommentParentType {
    POST, COMMENT
}