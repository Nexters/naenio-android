package com.nexters.teamversus.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NoticeListResponse(
    val notices: List<NoticeResponse>
)

@Keep
@Serializable
data class NoticeResponse(
    val id: Int,
    val title: String,
    val content: String
)