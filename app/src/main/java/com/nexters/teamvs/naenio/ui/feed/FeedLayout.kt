package com.nexters.teamvs.naenio.ui.feed

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Choice
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.Font.montserratSemiBold12
import com.nexters.teamvs.naenio.theme.Font.montserratSemiBold14
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.NaenioTypography
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile

@Composable
fun VoteBar(
    post: Post,
    onVote: (Int, Int) -> Unit,
) {
    val gageBarModifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .background(color = Color.Black, shape = RoundedCornerShape(16.dp))

    val fillGageBarModifier = Modifier.height(72.dp)
    var fillGageBarWidth = 200
    gageBarModifier.onGloballyPositioned { layoutCoordinates ->
        fillGageBarWidth = layoutCoordinates.size.width
    }
    val gageBarColorList = listOf(
        MyColors.purple_d669ff, MyColors.blue_5862ff, MyColors.blue_0bb9ff
    )

    /** 둘 중 하나라도 투표를 했는 지 */
    val isVotedForPost = post.isVotedForPost()

    Box(
        modifier = Modifier.wrapContentHeight(), contentAlignment = Alignment.Center
    ) {
        Column {
            GageBar(
                modifier = gageBarModifier,
                gageModifier = fillGageBarModifier,
                foregroundColor = Brush.horizontalGradient(
                    gageBarColorList
                ),
                choice = post.choice1,
                isVotedForPost = isVotedForPost,
                totalVoteCount = post.totalVoteCount,
                onVote = { voteId ->
                    onVote.invoke(post.id, voteId)
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            GageBar(
                modifier = gageBarModifier,
                gageModifier = fillGageBarModifier,
                foregroundColor = Brush.horizontalGradient(
                    gageBarColorList
                ),
                choice = post.choice2,
                isVotedForPost = isVotedForPost,
                totalVoteCount = post.totalVoteCount,
                onVote = { voteId ->
                    onVote.invoke(post.id, voteId)
                }
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_vs),
            contentDescription = "ic_vs",
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun GageBar(
    modifier: Modifier,
    choice: Choice,
    totalVoteCount: Int,
    gageModifier: Modifier,
    foregroundColor: Brush,
    isVotedForPost: Boolean,
    onVote: (Int) -> Unit
) {
    val isMyChoice = choice.isVoted

    Box(
        modifier = if (isMyChoice) {
            modifier.border(
                border = BorderStroke(1.dp, foregroundColor),
                shape = RoundedCornerShape(16.dp)
            ).noRippleClickable {
                onVote.invoke(choice.id)
            }
        } else {
            modifier.noRippleClickable {
                onVote.invoke(choice.id)
            }
        }
    ) {
        //What Box?
        Box(
            modifier = gageModifier
                .background(
                    brush = foregroundColor,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
//                .width(width = fillGageWidth * percent)
        )
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically
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
                text = choice.name,
                color = Color.White,
                style = Font.pretendardSemiBold14,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))

            /**
             * 투표 비율 및 인원
             */
            if (isVotedForPost) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = if(totalVoteCount > 0) {
                            "${((choice.voteCount.toFloat() / totalVoteCount.toFloat()) * 100).toInt()}%"
                        } else "0%",
                        color = Color.White, style = montserratSemiBold14
                    )
                    Text(
                        text = "${choice.voteCount} 명",
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
//    VoteBar(gage = 0.8f, true)
}

@Composable
fun VoteContent(post: Post? = null, modifier: Modifier, maxLine: Int) {
    post?.let { post ->
        Row(
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gift), contentDescription = "ic_gift"
            )
            Text(
                text = "${post.totalVoteCount}명 투표",
                color = Color.White,
                style = Font.body2,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(
            text = post.title,
            color = Color.White,
            style = Font.pretendardSemiBold20,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 29.sp
        )
        Text(
            text = post.content,
            color = Color.White,
            style = Font.pretendardRegular14,
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp),
            maxLines = maxLine,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CommentLayout(commentCount: Int = 0, modifier: Modifier) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
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
            text = "${commentCount}개",
            color = Color.White,
            style = Font.pretendardRegular16,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun ProfileNickName(
    modifier: Modifier, isIconVisible: Boolean, nickName: String = "", profileImageIndex: Int = 0
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImageIcon(index = profileImageIndex, size = 24.dp)
        Text(
            text = nickName,
            color = Color.White,
            style = Font.pretendardMedium16,
            modifier = Modifier
                .wrapContentWidth()
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
fun ProfileImageIcon(index: Int = 0, size: Dp, padding: Dp = 0.dp) {
    Image(
        painter = painterResource(id = Profile.images[index].image),
        contentDescription = "profile-img",
        modifier = Modifier
            .size(size)
            .background(MyColors.mint_83d8c8, shape = CircleShape)
            .padding(padding)
    )
}

@Composable
fun TopBar(
    modifier: Modifier,
    barTitle: String?,
    navController: NavHostController,
    isMoreBtnVisible: Boolean = true,
    textStyle: TextStyle = Font.pretendardSemiBold16
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            painter = painterResource(R.drawable.ic_back_left),
            contentDescription = "icon_back_m",
        )
        Spacer(modifier = Modifier.weight(1f))
        barTitle?.let { barTitle ->
            if (barTitle != "") {
                Text(
                    text = barTitle, style = textStyle, color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        val iconVisible = if (isMoreBtnVisible) 1f else 0f
        Image(modifier = Modifier
            .clickable {

            }
            .alpha(iconVisible),
            painter = painterResource(R.drawable.ic_feed_more),
            contentDescription = "icon_feed_more")
    }
}

@Composable
fun contentEmptyLayout(@StringRes stringId: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_empty), contentDescription = "icon_empty"
        )
        Text(
            modifier = Modifier.padding(top = 14.dp),
            text = stringResource(id = stringId),
            style = Font.pretendardMedium18,
            color = MyColors.darkGrey_828282
        )
    }
}
