package com.nexters.teamvs.naenio.domain.model

data class Profile(
    val id : Int,
    val nickname: String = "",
    val authServiceType : String,
    val profileImageIndex : Int = 0
)