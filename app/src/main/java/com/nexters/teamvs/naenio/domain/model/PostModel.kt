package com.nexters.teamvs.naenio.domain.model

import androidx.annotation.DrawableRes
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile

data class Post(
    val id: Int,
    val author: Author,
    val choices: List<Choice>,
    val commentCount: Int = 0,
    val content: String,
    val title: String
) {
    val choice1 = choices[0] //TODO DataLayer 에서 choices 리스트가 Size 2 인 것을 보장하기. 
    val choice2 = choices[1] 

    val totalVoteCount get() = choices.sumOf { it.voteCount }

    fun isVotedForPost(): Boolean {
        val votedChoice = choices.find { it.isVoted }
        return votedChoice != null
    }
}

data class Choice(
    val id: Int,
    val isVoted: Boolean = false,
    val name: String,
    val sequence: Int,
    val voteCount: Int = 0
)

data class Author(
    val id: Int,
    val nickname: String,
    val profileImageIndex: Int = 0
) {
    @DrawableRes val profileImage: Int = Profile.images[profileImageIndex].image

    companion object {
        val mock: Author = Author(
            id = -1,
            nickname = "???",
            profileImageIndex = 0
        )
    }
}