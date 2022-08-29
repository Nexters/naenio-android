package com.nexters.teamvs.naenio.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val nickname: String? = null,
    val profileImageIndex: Int = 0
)