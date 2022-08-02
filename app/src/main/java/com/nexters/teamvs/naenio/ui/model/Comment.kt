package com.nexters.teamvs.naenio.ui.model

data class Comment(
    val id: Int,
    val writer: User = User.mock,
    val postId: Int = 0,
    val replyCommentCount: Int = 0,
    val content: String,
    val like: Boolean = false,
    val likeCount: Long = 0,
    val writeTime: Long,
) {
    companion object {
        val mock = listOf<Comment>(
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Comment(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
        )
    }
}

data class ReplyComment(
    val id: Int,
    val parentId: Int,
    val writer: User,
    val likeCount: Int,
    val isLiked: Boolean,
    val writeTime: Long,
)
