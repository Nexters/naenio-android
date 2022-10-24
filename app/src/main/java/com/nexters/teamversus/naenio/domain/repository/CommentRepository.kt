package com.nexters.teamversus.naenio.domain.repository

import com.nexters.teamversus.naenio.data.network.api.CommentApi
import com.nexters.teamversus.naenio.data.network.api.FeedApi
import com.nexters.teamversus.naenio.data.network.dto.CommentParentType
import com.nexters.teamversus.naenio.data.network.dto.LikeCommentRequest
import com.nexters.teamversus.naenio.data.network.dto.ReportRequest
import com.nexters.teamversus.naenio.data.network.dto.WriteCommentRequest
import com.nexters.teamversus.naenio.domain.mapper.CommentMapper.toComment
import com.nexters.teamversus.naenio.domain.mapper.CommentMapper.toComments
import com.nexters.teamversus.naenio.domain.mapper.CommentMapper.toMyComment
import com.nexters.teamversus.naenio.domain.mapper.CommentMapper.toReplies
import com.nexters.teamversus.naenio.domain.mapper.CommentMapper.toReply
import com.nexters.teamversus.naenio.domain.mapper.ProfileMapper.toUser
import com.nexters.teamversus.naenio.domain.model.Author
import com.nexters.teamversus.naenio.domain.model.MyComments
import com.nexters.teamversus.naenio.ui.comment.Comment
import com.nexters.teamversus.naenio.ui.comment.Reply
import com.nexters.teamversus.naenio.utils.datastore.UserPreferencesRepository
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentApi: CommentApi,
    private val feedApi: FeedApi,
    private val userPreferencesRepository: UserPreferencesRepository,
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
        val user = userPreferencesRepository.getSyncUserPref().toUser()
        return response.toComment(
            Author(
                id = user.id,
                nickname = user.nickname ?: "No Name",
                profileImageIndex = user.profileImageIndex ?: 0
            )
        )
    }

    suspend fun getReplies(
        commentId: Int,
        size: Int,
        lastCommentId: Int?,
    ): List<Reply> {
        val response = commentApi.getCommentReplies(
            id = commentId,
            size = size,
            lastCommentId = lastCommentId,
        )
        return response.toReplies(commentId)
    }

    suspend fun writeReply(
        commentId: Int,
        content: String
    ): Reply {
        val response = commentApi.writeComment(
            WriteCommentRequest(
                parentId = commentId,
                parentType = CommentParentType.COMMENT,
                content = content,
            )
        )
        val user = userPreferencesRepository.getSyncUserPref().toUser()

        return response.toReply(
            Author(
                id = user.id,
                nickname = user.nickname ?: "No Name",
                profileImageIndex = user.profileImageIndex ?: 0
            )
        )
    }

    suspend fun deleteComment(id: Int) {
        commentApi.deleteComment(id)
    }

    suspend fun likeComment(id: Int) {
        commentApi.likeComment(LikeCommentRequest(id))
    }

    suspend fun unlikeComment(id: Int) {
        commentApi.unlikeComment(LikeCommentRequest(id))
    }

    suspend fun getMyCommentList(size: Int): List<MyComments> {
        return commentApi.getMyComment(size).toMyComment()
    }

    suspend fun report(reportRequest: ReportRequest) {
        feedApi.report(reportRequest)
    }
}