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

    fun WriteCommentResponse.toComment(): Comment {
        return Comment(
            id = id,
            postId = parentId,
            writer = Author.mock, //TODO 클라에서 캐싱한 데이터를 넣어주기.
            replyCount = 0,
            content = content,
            isLiked = false,
            likeCount = 0,
            writeTime = this.createdDateTime,
        )
    }

    fun ReplyResponse.toReplies(): List<Reply> {
        return commentReplies.map {
            Reply(
                id = it.id,
                writer = it.author.toAuthor(),
                content = it.content,
                isLiked = it.isLiked,
                likeCount = it.likeCount,
                writeTime = it.createdDatetime,
            )
        }
    }

    fun WriteCommentResponse.toReply(): Reply {
        return Reply(
            id = id,
            parentId = parentId,
            writer = Author.mock, //TODO 클라에서 캐싱한 데이터를 넣어주기.
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