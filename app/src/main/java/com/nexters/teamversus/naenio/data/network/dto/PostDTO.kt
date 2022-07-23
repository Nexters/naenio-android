package com.nexters.teamversus.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class WritePostRequest(
    val title: String,
    val content: String,
    val categoryId: Int,
    val choices: List<String> = emptyList()
)

@Serializable
data class PostResponse(
    val category: Category,
    val choices: List<Choice>,
    val content: String,
    val createdDateTime: String,
    val id: Int,
    val lastModifiedDateTime: String,
    val memberId: Int,
    val title: String
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

@Serializable
data class Choice(
    val id: Int,
    val name: String,
    val sequence: Int
)