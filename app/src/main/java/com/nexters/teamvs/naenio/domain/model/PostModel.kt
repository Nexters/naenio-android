package com.nexters.teamvs.naenio.domain.model

data class Post(
    val id: Int = -1,
    val author: Author? = null,
    val choices: List<Choice> = emptyList(),
    val commentCount: Int = 0,
    val content: String = "",
    val title: String = ""
)

data class Choice(
    val id: Int,
    val isVoted: Boolean,
    val name: String,
    val sequence: Int,
    val voteCount: Int
)

data class Author(
    val id: Int,
    val nickname: String?,
    val profileImageIndex: Int?
) {
    companion object {
        val mock: Author = Author(
            id = -1,
            nickname = "???",
            profileImageIndex = 0
        )
    }
}