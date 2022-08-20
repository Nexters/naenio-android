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
data class NoticeItem(
    val id: Int = -1,
    val title: String,
    val content: String
) {
    companion object {
        val noticeList = listOf<NoticeItem>(
            NoticeItem(
                id = 1,
                title = "네니오 출시 이벤트",
                content = "이벤트 페이지 댓글로 관심있는 연구자료를 인증하신 분들 30분을 추첨하여 스타벅스 아메리카노 기프티콘을 드립니다. 여러분들의 많은 참여 부탁드립니다!"
            ),
            NoticeItem(
                id = 2,
                title = "네니오 이벤트",
                content = "이벤트 페이지 댓글로 관심있는 연구자료를 인증하신 분들 30분을 추첨하여 스타벅스 아메리카노 기프티콘을 드립니다. 여러분들의 많은 참여 부탁드립니다!"

            )
        )
    }
}