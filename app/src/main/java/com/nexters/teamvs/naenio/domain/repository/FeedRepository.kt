package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toPostList
import com.nexters.teamvs.naenio.domain.model.Post
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedApi,
) : BaseRepository() {

    suspend fun getFeedPosts(
        pageSize: Int,
        lastPostId: Int?
    ): List<Post> {
        return feedApi.getFeedPosts(
            size = pageSize,
            lastPostId = lastPostId
        ).toPostList()
    }

}