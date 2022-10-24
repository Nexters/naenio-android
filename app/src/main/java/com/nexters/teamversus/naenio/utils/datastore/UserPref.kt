package com.nexters.teamversus.naenio.utils.datastore

data class UserPref(
    val id: Int,
    val authServiceType: String,
    val nickname: String? = null,
    val profileImageIndex: Int = 0
)