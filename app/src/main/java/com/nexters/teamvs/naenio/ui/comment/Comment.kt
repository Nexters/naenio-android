package com.nexters.teamvs.naenio.ui.comment

import com.nexters.teamvs.naenio.domain.model.Author

open class BaseComment(
    open val id: Int,
    open val writer: Author,
    open val content: String,
    open val likeCount: Int,
    open val isLiked: Boolean,
    open val writeTime: String,
)

data class Comment(
    override val id: Int,
    override val writer: Author = Author.mock,
    val postId: Int = 0,
    val replyCount: Int = 0,
    override val content: String,
    override val isLiked: Boolean = false,
    override val likeCount: Int = 0,
    override val writeTime: String,
): BaseComment(
    id = id,
    writer = writer,
    likeCount = likeCount,
    content = content,
    isLiked = isLiked,
    writeTime = writeTime
)

data class Reply(
    override val id: Int,
    val parentId: Int,
    override val content: String,
    override val writer: Author = Author.mock,
    override val likeCount: Int = 0,
    override val isLiked: Boolean = false,
    override val writeTime: String,
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
                parentId = -1,
                content = "그대로 하고 싶은데",
                writeTime = "123123123",
                isLiked = true
            ),
            Reply(
                id = 0,
                parentId = -1,
                content = "너의 얘기대로",
                writeTime = "123123123",
            ),
            Reply(
                id = 0,
                parentId = -1,
                content = "너 없이 행복하란 말",
                writeTime = "123123123",
                isLiked = true
            ),
        )
    }
}
