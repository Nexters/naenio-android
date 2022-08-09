package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.CommentResponse
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentResponse
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toAuthor
import com.nexters.teamvs.naenio.domain.model.Author
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
}