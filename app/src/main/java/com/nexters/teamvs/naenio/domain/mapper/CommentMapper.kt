package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.*
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toAuthor
import com.nexters.teamvs.naenio.domain.model.*
import com.nexters.teamvs.naenio.ui.comment.Comment
import com.nexters.teamvs.naenio.ui.comment.Reply

object CommentMapper {

    fun CommentResponse.toComments(postId: Int): List<Comment> {
        return comments.map {
            Comment(
                id = it.id,
                postId = postId,
                writer = it.author.toAuthor(),
                replyCount = it.repliesCount,
                content = it.content,
                isLiked = it.isLiked,
                likeCount = it.likeCount,
                writeTime = it.createdDatetime,
            )
        }
    }

    fun WriteCommentResponse.toComment(writer: Author): Comment {
        return Comment(
            id = id,
            postId = parentId,
            writer = writer,
            replyCount = 0,
            content = content,
            isLiked = false,
            likeCount = 0,
            writeTime = this.createdDateTime,
        )
    }

    fun ReplyResponse.toReplies(commentId: Int): List<Reply> {
        return commentReplies.map {
            Reply(
                id = it.id,
                parentId = commentId,
                writer = it.author.toAuthor(),
                content = it.content,
                isLiked = it.isLiked,
                likeCount = it.likeCount,
                writeTime = it.createdDatetime,
            )
        }
    }

    fun WriteCommentResponse.toReply(writer: Author): Reply {
        return Reply(
            id = id,
            parentId = parentId,
            writer = writer,
            content = content,
            isLiked = false,
            likeCount = 0,
            writeTime = this.createdDateTime,
        )
    }

    fun MyCommentPostResponse.toCommentPost() : CommentPost {
        return CommentPost(
            id = id,
            title = title,
            author = author.toAuthor()
        )
    }

    fun MyCommentResponse.toMyComment() : List<MyComments> {
        return comments.map {
            MyComments(
                id = it.id,
                content = it.content,
                post = it.post.toCommentPost()
            )
        }
    }
}