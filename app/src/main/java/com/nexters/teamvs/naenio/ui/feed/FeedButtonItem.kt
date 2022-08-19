package com.nexters.teamvs.naenio.ui.feed

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.TypedArrayUtils.getString
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.NaenioApp
import com.nexters.teamvs.naenio.theme.MyColors

data class FeedButtonItem(
    val id : Int = -1,
    val title: String = "",
    val emptyMessage: String = "",
    var isSelected : Boolean = false,
    @DrawableRes val image: Int? = null,
    val type : String = ""
) {
    companion object {
        val feedButtonList = listOf<FeedButtonItem>(
            FeedButtonItem(
                id = 0,
                title = NaenioApp.context.getString(R.string.feed_all_vote),
                emptyMessage = NaenioApp.context.getString(R.string.feed_empty),
                isSelected = true,
                type = "ALL"
            ),
            FeedButtonItem(
                id = 1,
                title = NaenioApp.context.getString(R.string.feed_my_posted_vote),
                emptyMessage = NaenioApp.context.getString(R.string.my_posted_vote_empty),
                type = "MY_POST",
                image = R.drawable.icon_posted
            ),
            FeedButtonItem(
                id = 2,
                title = NaenioApp.context.getString(R.string.feed_my_vote),
                emptyMessage = NaenioApp.context.getString(R.string.my_vote_empty),
                type = "VOTED_BY_ME",
                image = R.drawable.icon_participated
            )
        )
    }
}