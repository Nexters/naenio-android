package com.nexters.teamvs.naenio.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors

//TODO 테마 목록 조회 API 연동, 리스폰스모델 -> 해당 모델로 변환해서 사용하현
data class ThemeItem(
    val id: Int = -1,
    val title: String,
    val description: String,
    val backgroundColor: Color,
    @DrawableRes val image: Int
) {
    companion object {
        val mock = listOf<ThemeItem>(
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
            ThemeItem(
                title = "title",
                description = "description",
                backgroundColor = MyColors.pink,
                image = R.drawable.ic_heart_outlined
            ),
        )
    }
}