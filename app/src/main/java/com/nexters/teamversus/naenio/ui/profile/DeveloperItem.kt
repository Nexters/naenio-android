package com.nexters.teamversus.naenio.ui.profile

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.TypedArrayUtils.getString
import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.base.NaenioApp
import com.nexters.teamversus.naenio.theme.MyColors

data class DeveloperItem(
    val title: String,
    val content: String,
) {
    companion object {
        val developerList = listOf<DeveloperItem>(
            DeveloperItem(
                title = "Product Manager",
                content = "이영빈"
            ),
            DeveloperItem(
                title = "Designer",
                content = "박주리, 곽민주"
            ),
            DeveloperItem(
                title = "iOS Developer",
                content = "이영빈, 조윤영"
            ),
            DeveloperItem(
                title = "Android Developer",
                content = "오해성, 김유나"
            ),
            DeveloperItem(
                title = "Backend Developer",
                content = "김경준"
            ),
            DeveloperItem(
                title = "Frontend Developer",
                content = "김경준"
            )
        )
    }
}