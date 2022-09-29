package com.nexters.teamversus.naenio.data.network.dto

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class ReportType {
    POST, COMMENT
}

@Keep
@Serializable
data class ReportRequest(
    val targetMemberId: Int,
    val resource: ReportType
)