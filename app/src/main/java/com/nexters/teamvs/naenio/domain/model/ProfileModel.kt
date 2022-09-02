package com.nexters.teamvs.naenio.domain.model

data class Profile(
    val id : Int,
    val nickname: String? = null,
    val authServiceType : String,
    val profileImageIndex : Int = 0
)

data class Notice(
    val id : Int,
    val title : String,
    val content : String
)