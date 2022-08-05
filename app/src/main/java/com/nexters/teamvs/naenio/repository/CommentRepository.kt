package com.nexters.teamvs.naenio.repository

import com.nexters.teamvs.naenio.data.network.api.CommentApi
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.data.network.dto.PostCommentResponse
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentRequest
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentApi: CommentApi,
) {

    suspend fun getComments(
        id: Int,
        size: Int,
        lastCommentId: Int,
    ) {
        commentApi.getComments(
            id = id,
            size = size,
            lastCommentId = lastCommentId
        )
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