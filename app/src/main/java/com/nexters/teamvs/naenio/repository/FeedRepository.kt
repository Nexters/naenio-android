package com.nexters.teamvs.naenio.repository

import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.data.network.api.UserApi
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.data.network.dto.FeedRequest
import com.nexters.teamvs.naenio.data.network.dto.WriteCommentRequest
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedApi,
) : BaseRepository() {

    suspend fun getFeedPosts(
        pageSize: Int,
        lastPostId: Int?
    ) {
        feedApi.getFeedPosts(
            size = pageSize,
            lastPostId = lastPostId
        )
    }

}