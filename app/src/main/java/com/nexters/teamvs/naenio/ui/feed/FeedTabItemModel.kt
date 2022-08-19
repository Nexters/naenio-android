package com.nexters.teamvs.naenio.ui.feed

import androidx.annotation.DrawableRes

data class FeedTabItemModel(
    val title: String,
    @DrawableRes val image: Int?,
    val type : FeedTabItemType
)

enum class FeedTabItemType(val text: String) {
    All("ALL"), MyPost("MY_POST"), MyVote("VOTED_BY_ME")
}