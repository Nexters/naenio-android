package com.nexters.teamvs.naenio.ui.model

data class User(
    val id: Int,
    val nickname: String?,
    val profileImageIndex: Int = 0
) {
    companion object {
        val mock = User(0, "닉네임")
    }
}