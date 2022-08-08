package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.data.network.api.CommentApi
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentRequest
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
        parentId: String,
        parentType: CommentParentType,
        content: String
    ) {
        commentApi.writeComment(
            WriteCommentRequest(
                parentId = parentId,
                parentType = parentType,
                content = content,
            )
        )
    }
}