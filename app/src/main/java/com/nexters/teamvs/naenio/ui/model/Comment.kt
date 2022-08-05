package com.nexters.teamvs.naenio.ui.model

open class BaseComment(
    open val id: Int,
    open val writer: User,
    open val content: String,
    open val likeCount: Long,
    open val isLiked: Boolean,
    open val writeTime: Long,
)

data class Comment(
    override val id: Int,
    override val writer: User = User.mock,
    val postId: Int = 0,
    val replyCount: Int = 0,
    override val content: String,
    override val isLiked: Boolean = false,
    override val likeCount: Long = 0,
    override val writeTime: Long,
): BaseComment(
    id = id,
    writer = writer,
    likeCount = likeCount,
    content = content,
    isLiked = isLiked,
    writeTime = writeTime
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

data class Reply(
    override val id: Int,
    val parentId: Int = -1,
    override val content: String,
    override val writer: User = User.mock,
    override val likeCount: Long = 0,
    override val isLiked: Boolean = false,
    override val writeTime: Long,
): BaseComment(
    id = id,
    content = content,
    writer = writer,
    likeCount = likeCount,
    isLiked = isLiked,
    writeTime = writeTime
) {
    companion object {
        val mock = listOf<Reply>(
            Reply(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Reply(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
            Reply(
                id = 0,
                content = "댓글댓글댓글댓글댓글",
                writeTime = 123123123,
            ),
        )
    }
}
