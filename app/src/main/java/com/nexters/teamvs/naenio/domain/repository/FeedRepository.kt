package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toPost
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toPostList
import com.nexters.teamvs.naenio.domain.model.Post
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val feedApi: FeedApi,
) : BaseRepository() {

    suspend fun getFeedPosts(
        pageSize: Int,
        lastPostId: Int?,
        sortType: String?
    ): List<Post> {
        return feedApi.getFeedPosts(
            size = pageSize,
            lastPostId = lastPostId,
            sortType = sortType
        ).toPostList()
    }

    suspend fun getThemePosts(
        theme : String
    ): List<Post> {
        return feedApi.getThemePosts(theme).toPostList()
    }

    suspend fun getRandomPosts() : Post {
        return feedApi.getRandomPost().toPost()
    }

}