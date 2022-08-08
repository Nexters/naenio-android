package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.FeedApi
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