package com.nexters.teamvs.naenio.domain.model

data class Profile(
    val id : Int = -1,
    val nickname: String = "",
    val authServiceType : String = "",
    val profileImageIndex : Int = -1
)