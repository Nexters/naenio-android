package com.nexters.teamversus.naenio.domain.model

data class MyCommentList(
    val comments: List<MyComments>
)

data class MyComments(
    val id: Int,
    val content: String,
    val post: CommentPost
)

data class CommentPost(
    val id: Int,
    val author: Author,
    val title: String
)