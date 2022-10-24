package com.nexters.teamversus.naenio.utils.datastore

data class UserPref(
    val id: Int,
    val authServiceType: String,
    val nickname: String?,
    val profileImageIndex: Int?
)