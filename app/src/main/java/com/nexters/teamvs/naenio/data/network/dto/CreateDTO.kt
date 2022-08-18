package com.nexters.teamvs.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequest(
    val choices: List<Choice>,
    val content: String,
    val title: String
) {
    @Serializable
    data class Choice(
        val name: String
    )
}

@Serializable
data class CreateResponse(
    val choices: List<Choice>,
    val content: String,
    val createdDateTime: String,
    val id: Int,
    val lastModifiedDateTime: String,
    val memberId: Int,
    val title: String
) {
    @Serializable
    data class Choice(
        val id: Int,
        val name: String,
        val sequence: Int
    )
}