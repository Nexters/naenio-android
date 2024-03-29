package com.nexters.teamversus.naenio.domain.repository

import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.base.BaseRepository
import com.nexters.teamversus.naenio.base.NaenioApp
import com.nexters.teamversus.naenio.data.network.api.FeedApi
import com.nexters.teamversus.naenio.data.network.dto.CreateRequest
import com.nexters.teamversus.naenio.data.network.dto.ReportRequest
import com.nexters.teamversus.naenio.data.network.dto.VoteRequest
import com.nexters.teamversus.naenio.domain.mapper.FeedMapper.toPost
import com.nexters.teamversus.naenio.domain.mapper.FeedMapper.toPostList
import com.nexters.teamversus.naenio.domain.model.Post
import com.nexters.teamversus.naenio.ui.feed.FeedTabItemModel
import com.nexters.teamversus.naenio.ui.feed.FeedTabItemType
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
        ).posts.toPostList()
    }

    suspend fun getThemePosts(
        theme: String
    ): List<Post> {
        return feedApi.getThemePosts(theme).posts.toPostList()
    }

    suspend fun getRandomPosts(): Post {
        return feedApi.getRandomPost().toPost()
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

    suspend fun deletePost(postId: Int) {
        feedApi.deletePost(postId)
    }

    suspend fun vote(
        postId: Int,
        choiceId: Int,
    ) {
        feedApi.vote(VoteRequest(postId = postId, choiceId = choiceId))
    }

    fun getFeedTabItems(): List<FeedTabItemModel> {
        return listOf(
            FeedTabItemModel(
                title = NaenioApp.context.getString(R.string.feed_all_vote),
                type = FeedTabItemType.All,
                image = null
            ),
            FeedTabItemModel(
                title = NaenioApp.context.getString(R.string.feed_my_posted_vote),
                type = FeedTabItemType.MyPost,
                image = R.drawable.icon_posted
            ),
            FeedTabItemModel(
                title = NaenioApp.context.getString(R.string.feed_my_vote),
                type = FeedTabItemType.MyVote,
                image = R.drawable.ic_gift
            )
        )
    }

    suspend fun getPostDetail(
        id: Int
    ): Post {
        return feedApi.getPostDetail(id).toPost()
    }

    suspend fun report(reportRequest: ReportRequest) {
        feedApi.report(reportRequest)
    }
}