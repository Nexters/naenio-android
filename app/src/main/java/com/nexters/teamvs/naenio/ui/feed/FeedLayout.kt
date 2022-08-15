package com.nexters.teamvs.naenio.ui.feed

import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.Font.montserratSemiBold12
import com.nexters.teamvs.naenio.theme.Font.montserratSemiBold14
import com.nexters.teamvs.naenio.theme.Font.montserratSemiBold16
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.NaenioTypography

@Composable
fun VoteGageBar(gage : Float, isCountVisible : Boolean) {
    val gageBarModifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .background(color = Color.Black, shape = RoundedCornerShape(16.dp))

    val fillGageBarModifier = Modifier
        .height(72.dp)
    var fillGageBarWidth = 200
    gageBarModifier.onGloballyPositioned { layoutCoordinates ->
        fillGageBarWidth = layoutCoordinates.size.width
    }
    val gageBarColorList =  listOf(
        MyColors.purple_d669ff,
        MyColors.blue_5862ff,
        MyColors.blue_0bb9ff
    )
    Box(
        modifier = Modifier
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            GageBar(
                modifier = gageBarModifier,
                gageModifier = fillGageBarModifier,
                foregroundColor = Brush.horizontalGradient(
                    gageBarColorList
                ),
                percent = gage,
                fillGageWidth = Dp(fillGageBarWidth * gage),
                isCountVisible = isCountVisible
            )
            Spacer(modifier = Modifier.height(18.dp))
            GageBar(
                modifier = gageBarModifier,
                gageModifier = fillGageBarModifier,
                foregroundColor = Brush.horizontalGradient(
                    gageBarColorList
                ),
                percent = gage,
                fillGageWidth = Dp(fillGageBarWidth * gage),
                isCountVisible = isCountVisible
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_vs),
            contentDescription = "ic_vs",
            modifier = Modifier.padding(vertical = 6.dp))
    }
}

@Composable
fun GageBar(
    modifier: Modifier,
    gageModifier: Modifier,
    foregroundColor: Brush,
    percent: Float,
    fillGageWidth: Dp,
    isCountVisible: Boolean
) {
    Box(
        modifier = modifier
            .border(border = BorderStroke(1.dp, foregroundColor), shape = RoundedCornerShape(16.dp)) // 사용자가 선택할 경우
    ) {
        Box(
            modifier = gageModifier
                .background(
                    foregroundColor,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                .width(width = fillGageWidth * percent)
        )
        Row(modifier = Modifier
            .wrapContentSize()
            .align(Alignment.Center)
            .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "A.",
                color = Color.White,
                style = NaenioTypography.h4
            )
            Text(
                modifier = Modifier
                    .padding(start = 6.dp, end = 8.dp)
                    .weight(1f),
                text = "투명인간 취급당하며 힘들게 살기투명인간 취급당하며 힘들게 살기투명인간 취급당하며 힘들게 살기투명인간 취급당하며 힘들게 살기",
                color = Color.White,
                style = Font.pretendardSemiBold14,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (isCountVisible) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = "70%", // "$percent%",
                        color = Color.White,
                        style = montserratSemiBold14
                    )
                    Text(
                        text = "70" + "명",
                        color = MyColors.grey979797,
                        style = montserratSemiBold12
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun VoteLaout() {
    VoteGageBar(gage = 0.8f, true)
}

@Composable
fun VoteContent(modifier: Modifier, maxLine : Int) {
    Row(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_gift),
            contentDescription = "ic_gift"
        )
        Text(
            text = "XXX",
            color = Color.White,
            style = Font.body2,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "명 투표",
            color = Color.White,
            style = Font.body2
        )
    }
    Text(
        text = "세상에 모든 사람이 날 알아보기 투명인간 취급 당하기????",
        color = Color.White,
        style = Font.pretendardSemiBold20,
        modifier = Modifier.padding(top = 8.dp),
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 29.sp
    )
    Text(
        text = "세상 모든 사람들이 날 알아보지 못하면 슬플 것 같아요. ㅠㅠ",
        color = Color.White,
        style = Font.pretendardRegular14,
        modifier = Modifier.padding(top = 10.dp, bottom = 8.dp),
        maxLines = maxLine,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CommentLayout(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_comment_icon),
            contentDescription = "ic_comment_icon"
        )
        Text(
            text = "댓글",
            color = Color.White,
            style = Font.pretendardSemiBold16,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "XXX",
            color = Color.White,
            style = Font.pretendardRegular16,
            modifier = Modifier.padding(start = 6.dp)
        )
        Text(
            text = "개",
            color = Color.White,
            style = Font.pretendardRegular16
        )
    }
}

@Composable
fun ProfileNickName(modifier: Modifier, isIconVisible : Boolean) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageIcon(size = 24.dp)
        Text(
            text = "닉네임",
            color = Color.White,
            style = Font.pretendardMedium16,
            modifier = Modifier.wrapContentWidth()
                .padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isIconVisible) {
            Image(
                painter = painterResource(R.drawable.ic_feed_more),
                contentDescription = "icon_feed_more"
            )
        }
    }
}

@Composable
fun ProfileImageIcon(size : Dp) {
    //TODO 프로필 이미지 타입 정의
    Icon(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        tint = MyColors.mint_83d8c8,
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "profileThumbnail"
    )
}

@Composable
fun TopBar(modifier: Modifier,
           barTitle: String,
           navController: NavHostController,
           isMoreBtnVisible : Boolean = true) {
    Row(modifier = modifier
        .padding(horizontal = 24.dp)
        .fillMaxWidth()
        .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable {

            },
            painter = painterResource(R.drawable.ic_back_left),
            contentDescription = "icon_back_m",
        )
        Spacer(modifier = Modifier.weight(1f))
        if (barTitle != "") {
            Text(
                text = barTitle,
                style = Font.pretendardSemiBold16,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        val iconVisible = if(isMoreBtnVisible) 1f else 0f
        Image(
            modifier = Modifier.clickable {

            }.alpha(iconVisible),
            painter = painterResource(R.drawable.ic_feed_more),
            contentDescription = "icon_feed_more"
        )
    }
}
