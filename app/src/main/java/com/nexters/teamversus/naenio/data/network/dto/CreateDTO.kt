package com.nexters.teamversus.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CreateRequest(
    val choices: List<Choice>,
    val content: String,
    val title: String
) {
    @Keep
    @Serializable
    data class Choice(
        val name: String
    )
}

@Keep
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
    @Keep
    @Serializable
    data class Choice(
        val id: Int,
        val name: String,
        val sequence: Int
    )
}