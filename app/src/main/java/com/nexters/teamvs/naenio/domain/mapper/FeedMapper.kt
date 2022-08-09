package com.nexters.teamvs.naenio.domain.mapper

import com.nexters.teamvs.naenio.data.network.dto.AuthorResponse
import com.nexters.teamvs.naenio.data.network.dto.ChoiceResponse
import com.nexters.teamvs.naenio.data.network.dto.PostResponse
import com.nexters.teamvs.naenio.domain.model.Author
import com.nexters.teamvs.naenio.domain.model.Choice
import com.nexters.teamvs.naenio.domain.model.Post

object FeedMapper {

    fun AuthorResponse.toAuthor(): Author {
        return Author(
            id = id,
            nickname = nickname,
            profileImageIndex = profileImageIndex,
        )
    }

    fun List<ChoiceResponse>.toChoiceList(): List<Choice> {
        return map {
            Choice(
                id = it.id,
                isVoted = it.isVoted,
                name = it.name,
                sequence = it.sequence,
                voteCount = it.voteCount
            )
        }
    }
    fun List<PostResponse>.toPostList(): List<Post> {
        return map {
            Post(
                id = it.id,
                author = it.author.toAuthor(),
                commentCount = it.commentCount,
                content = it.content,
                title = it.title,
                choices = it.choices.toChoiceList()
            )
        }
    }
}