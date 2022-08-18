package com.nexters.teamvs.naenio.domain.model

import androidx.annotation.DrawableRes
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile

data class Post(
    val id: Int,
    val author: Author,
    val choices: List<Choice>,
    val commentCount: Int = 0,
    val content: String = "",
    val title: String
)

data class Choice(
    val id: Int,
    val isVoted: Boolean = false,
    val name: String,
    val sequence: Int,
    val voteCount: Int = 0
)

data class Author(
    val id: Int,
    val nickname: String?,
    private val profileImageIndex: Int = 0
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