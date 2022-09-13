package com.nexters.teamvs.naenio.ui.comment

import com.nexters.teamvs.naenio.domain.model.Author

open class BaseComment(
    open val id: Int,
    open val writer: Author,
    open val content: String,
    open val likeCount: Int,
    open val isLiked: Boolean,
    open val displayTime: String,
    private val writeTime: String,
)

data class Comment(
    override val id: Int,
    override val writer: Author = Author.mock,
    val postId: Int = 0,
    val replyCount: Int = 0,
    override val content: String,
    override val isLiked: Boolean = false,
    override val likeCount: Int = 0,
    private val writeTime: String,
    override val displayTime: String,
): BaseComment(
    id = id,
    writer = writer,
    likeCount = likeCount,
    content = content,
    isLiked = isLiked,
    writeTime = writeTime,
    displayTime = displayTime
)

data class Reply(
    override val id: Int,
    val parentId: Int,
    override val content: String,
    override val writer: Author = Author.mock,
    override val likeCount: Int = 0,
    override val isLiked: Boolean = false,
    private val writeTime: String,
    override val displayTime: String,
): BaseComment(
    id = id,
    content = content,
    writer = writer,
    likeCount = likeCount,
    isLiked = isLiked,
    writeTime = writeTime,
    displayTime = displayTime
) {
    companion object {
        val mock = listOf<Reply>(

        )
    }
}
