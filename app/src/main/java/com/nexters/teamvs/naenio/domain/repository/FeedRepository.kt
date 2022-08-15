package com.nexters.teamvs.naenio.domain.repository

import com.nexters.teamvs.naenio.base.BaseRepository
import com.nexters.teamvs.naenio.data.network.api.FeedApi
import com.nexters.teamvs.naenio.data.network.dto.CreateRequest
import com.nexters.teamvs.naenio.data.network.dto.CreateResponse
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toPost
import com.nexters.teamvs.naenio.domain.mapper.FeedMapper.toPostList
import com.nexters.teamvs.naenio.domain.model.Author
import com.nexters.teamvs.naenio.domain.model.Choice
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

    suspend fun createPost(
        title: String,
        content: String,
        choices: Array<String>
    ): Post {
        val response = feedApi.createPost(
            CreateRequest(
                title = title,
                content = content,
                choices = choices.map {
                    CreateRequest.Choice(it)
                }
            )
        )
        return response.toPost()
    }
}