package com.nexters.teamvs.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class LikeCommentRequest(val commentId: Int)

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
    val content: String = "ewrrqw", //TODO 현재 서버에서 필드 안내려주는 중
    val id: Int,
    val isLiked: Boolean,
    val likeCount: Int,
    val repliesCount: Int
)

@Serializable
@Keep
data class WriteCommentRequest(
    val parentId: Int,
    val parentType: CommentParentType,
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
    //TODO 답글 총 갯수도 필요?
)

@Serializable
@Keep
data class CommentReply(
    val author: AuthorResponse,
    val createdDatetime: String,
    val id: Int,
    val isLiked: Boolean,
    val likeCount: Int,
    val content: String,
)

@Serializable
@Keep
enum class CommentParentType {
    POST, COMMENT
}