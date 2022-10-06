package com.nexters.teamversus.naenio.domain.model

data class User(
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