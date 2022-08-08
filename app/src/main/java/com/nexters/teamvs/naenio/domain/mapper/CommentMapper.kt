package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.CommentResponse
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toAuthor
import com.nexters.teamvs.naenio.ui.comment.Comment

object CommentMapper {

    fun CommentResponse.toComments(postId: Int) : List<Comment> {
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
}