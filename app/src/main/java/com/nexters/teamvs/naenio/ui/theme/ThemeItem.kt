package com.nexters.teamvs.naenio.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.NaenioApp
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.theme.ThemeItem.Companion.themeList

enum class ThemeType {
    COLLAPSED_BALANCE, NOISY, GOLD_BALANCE, RANDOM_PLAY, TODAY_VOTE, HALL_OF_FAME
}

fun ThemeType.findTheme(): ThemeItem {
    return themeList.find { it.type == this }!! // ThemeItem 을 로컬에서 관리하기 때문에 Nonnull 보장 가능
}

data class ThemeItem(
    val id : Int,
    val title: String,
    val description: String,
    val backgroundColor: Color = Color.White,
    val backgroundColorList : List<Color>,
    @DrawableRes val image: Int,
    val type : ThemeType
) {
    companion object {
        val themeList = listOf(
            ThemeItem(
                id = 1,
                title = NaenioApp.context.getString(R.string.theme_item_title1),
                description = NaenioApp.context.getString(R.string.theme_item_content1),
                backgroundColor = MyColors.pink,
                image = R.drawable.card_01,
                backgroundColorList = listOf(
                    Color(0xffeeaaff),
                    Color(0xffc9c4f9),
                    Color(0xff6dafe9)
                ),
                type = ThemeType.TODAY_VOTE
            ),
            ThemeItem(
                id = 2,
                title = NaenioApp.context.getString(R.string.theme_item_title2),
                description = NaenioApp.context.getString(R.string.theme_item_content2),
                backgroundColor = MyColors.green_24CE9E,
                image = R.drawable.card_2,
                backgroundColorList = listOf(
                    Color(0xff24ce9e),
                    Color(0xffaafffa),
                    Color(0xff6dafe9)
                ),
                type = ThemeType.HALL_OF_FAME
            ),
            ThemeItem(
                id = 3,
                title = NaenioApp.context.getString(R.string.theme_item_title3),
                description = NaenioApp.context.getString(R.string.theme_item_content3),
                backgroundColor = MyColors.blue_5862ff,
                image = R.drawable.card_3,
                backgroundColorList = listOf(
                    Color(0xff5862ff),
                    Color(0xffa58eff),
                    Color(0xff34a1ff)
                ),
                type = ThemeType.RANDOM_PLAY
            ),
            ThemeItem(
                id = 4,
                title = NaenioApp.context.getString(R.string.theme_item_title4),
                description = NaenioApp.context.getString(R.string.theme_item_content4),
                backgroundColor = MyColors.orange_FFA927,
                image = R.drawable.card_4,
                backgroundColorList = listOf(
                    Color(0xffffa927),
                    Color(0xffffddaa),
                    Color(0xff6dafe9)
                ),
                type = ThemeType.GOLD_BALANCE
            ),
            ThemeItem(
                id = 5,
                title = NaenioApp.context.getString(R.string.theme_item_title5),
                description = NaenioApp.context.getString(R.string.theme_item_content5),
                backgroundColor = MyColors.peach_FF9C80,
                image = R.drawable.card_5,
                backgroundColorList = listOf(
                    Color(0xffff9c80),
                    Color(0xffffc9aa),
                    Color(0xff6dafe9)
                ),
                type = ThemeType.NOISY
            ),
            ThemeItem(
                id = 6,
                title = NaenioApp.context.getString(R.string.theme_item_title6),
                description = NaenioApp.context.getString(R.string.theme_item_content6),
                backgroundColor = MyColors.blue_26BDD9,
                image = R.drawable.card_6,
                backgroundColorList = listOf(
                    Color(0xff26bdd9),
                    Color(0xffaff3ff),
                    Color(0xff6d97e9)
                ),
                type = ThemeType.COLLAPSED_BALANCE
            ),
        )
    }
}