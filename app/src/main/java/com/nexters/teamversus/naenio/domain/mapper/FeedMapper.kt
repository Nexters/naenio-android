package com.nexters.teamversus.naenio.domain.mapper

import com.nexters.teamversus.naenio.data.network.dto.AuthorResponse
import com.nexters.teamversus.naenio.data.network.dto.ChoiceResponse
import com.nexters.teamversus.naenio.data.network.dto.CreateResponse
import com.nexters.teamversus.naenio.data.network.dto.PostResponse
import com.nexters.teamversus.naenio.domain.mapper.FeedMapper.toAuthor
import com.nexters.teamversus.naenio.domain.mapper.FeedMapper.toChoiceList
import com.nexters.teamversus.naenio.domain.model.Author
import com.nexters.teamversus.naenio.domain.model.Choice
import com.nexters.teamversus.naenio.domain.model.Post

object FeedMapper {

    fun AuthorResponse.toAuthor(): Author {
        return Author(
            id = id,
            nickname = nickname,
            profileImageIndex = profileImageIndex ?: 0,
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

    fun PostResponse.toPost() : Post {
        return Post(
            author = author.toAuthor(),
            choices = choices.toChoiceList(),
            commentCount = commentCount,
            content = content,
            id = id,
            title = title
        )
    }

    fun CreateResponse.toPost(): Post {
        return Post(
            id = id,
            author = Author.mock,
            choices = choices.mapIndexed { i, it ->
                Choice(
                    id = it.id,
                    isVoted = false,
                    name = it.name,
                    sequence = i
                )
            },
            content = content,
            title = title
        )
    }
}