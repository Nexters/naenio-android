package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.data.network.api.CommentApi
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentRequest
import com.nexters.teamvs.naenio.domain.mapper.CommentMapper.toComment
import com.nexters.teamvs.naenio.domain.mapper.CommentMapper.toComments
import com.nexters.teamvs.naenio.ui.comment.Comment
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentApi: CommentApi,
) {
    suspend fun getComments(
        postId: Int,
        size: Int,
        lastCommentId: Int?,
    ): List<Comment> {
        return commentApi.getComments(
            id = postId,
            size = size,
            lastCommentId = lastCommentId
        ).toComments(postId)
    }

    suspend fun writeComment(
        postId: Int,
        content: String
    ): Comment {
        val response = commentApi.writeComment(
            WriteCommentRequest(
                parentId = postId,
                parentType = CommentParentType.POST,
                content = content,
            )
        )
        return response.toComment()
    }

    suspend fun writeReply(
        commentId: Int,
        content: String
    ): Comment {
        val response = commentApi.writeComment(
            WriteCommentRequest(
                parentId = commentId,
                parentType = CommentParentType.COMMENT,
                content = content,
            )
        )
        return response.toComment()
    }
}