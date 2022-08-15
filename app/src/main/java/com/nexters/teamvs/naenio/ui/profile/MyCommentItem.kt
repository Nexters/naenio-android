package com.nexters.teamvs.naenio.ui.profile

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.TypedArrayUtils.getString
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.NaenioApp
import com.nexters.teamvs.naenio.theme.MyColors

// TODO 리스폰스모델 -> 해당 모델로 변환해서 사용하..?
data class MyCommentItem(
    val id: Int = -1,
    val feedWriter: String,
    val feedTitle: String,
    val myComment: String,
    @DrawableRes val feedWriterImage: Int
) {
    companion object {
        val myCommentList = listOf<MyCommentItem>(
            MyCommentItem(
                id = 1,
                feedWriter = "김만두",
                feedTitle = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기세상에 모든 사람이 날 알아보기 투명인간 취급 당하기",
                myComment = "그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. ",
                feedWriterImage = R.drawable.card_4
            ),
            MyCommentItem(
                id = 1,
                feedWriter = "김만두",
                feedTitle = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기세상에 모든 사람이 날 알아보기 투명인간 취급 당하기",
                myComment = "그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. ",
                feedWriterImage = R.drawable.card_4
            ),
            MyCommentItem(
                id = 1,
                feedWriter = "김만두",
                feedTitle = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기세상에 모든 사람이 날 알아보기 투명인간 취급 당하기",
                myComment = "그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. ",
                feedWriterImage = R.drawable.card_4
            ),
            MyCommentItem(
                id = 1,
                feedWriter = "김만두",
                feedTitle = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기세상에 모든 사람이 날 알아보기 투명인간 취급 당하기",
                myComment = "그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. ",
                feedWriterImage = R.drawable.card_4
            ),
            MyCommentItem(
                id = 1,
                feedWriter = "김만두",
                feedTitle = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기세상에 모든 사람이 날 알아보기 투명인간 취급 당하기",
                myComment = "그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. 그건 너무 슬플 것 같아 .. ",
                feedWriterImage = R.drawable.card_4
            )
        )
    }
}