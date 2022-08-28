package com.nexters.teamvs.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoticeListResponse(
    val notices: List<NoticeResponse>
)

@Serializable
data class NoticeResponse(
    val id: Int,
    val title: String,
    val content: String
)