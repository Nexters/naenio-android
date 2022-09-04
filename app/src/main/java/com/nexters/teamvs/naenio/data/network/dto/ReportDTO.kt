package com.nexters.teamvs.naenio.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
enum class ReportType {
    POST, COMMENT
}

@Serializable
data class ReportRequest(
    val targetMemberId: Int,
    val resource: ReportType
)