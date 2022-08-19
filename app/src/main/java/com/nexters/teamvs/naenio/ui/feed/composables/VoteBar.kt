package com.nexters.teamvs.naenio.ui.feed.composables

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Choice
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.NaenioTypography
import kotlinx.coroutines.delay

@Composable
fun VoteBar(
    post: Post,
    onVote: (Int, Int) -> Unit,
) {
    /** 둘 중 하나라도 투표를 했는 지 */
    val isVotedForPost = post.isVotedForPost()

    Box(
        modifier = Modifier.wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            GageBar(
                indexText = "A.",
                choice = post.choice1,
                isVotedForPost = isVotedForPost,
                totalVoteCount = post.totalVoteCount,
                onVote = { voteId ->
                    onVote.invoke(post.id, voteId)
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            GageBar(
                indexText = "B.",
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
//    modifier: Modifier,
    choice: Choice,
    indexText: String,
//    fillGageBarWidth: Dp,
    totalVoteCount: Int,
//    gageModifier: Modifier,
//    foregroundColor: Brush,
    isVotedForPost: Boolean,
    onVote: (Int) -> Unit
) {
    val isMyChoice = choice.isVoted

    val modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)

    val gradientColors = listOf(
        MyColors.purple_d669ff, MyColors.blue_5862ff, MyColors.blue_0bb9ff
    )

    //java.lang.IllegalArgumentException: Cannot round NaN value.
    val percent = ((choice.voteCount.toFloat() / totalVoteCount.toFloat()))
//    val precent = ((choice.voteCount.toFloat() / totalVoteCount.toFloat())) + 0.0f
    var progress by remember { mutableStateOf(0.0f) }
    progress = percent

    fun Float.isNon(double: Float):Boolean {
        return double != double
    }
//    LaunchedEffect(isVotedForPost) {
//        while ((progress <= percent)) {
//            progress += 0.05f
//            delay(10)
//        }
//    }

    Box(
        modifier = if (isMyChoice) {
            modifier
                .background(color = Color.Black, shape = RoundedCornerShape(16.dp))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            gradientColors
                        ),
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .noRippleClickable {
                    onVote.invoke(choice.id)
                }
        } else {
            modifier
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .noRippleClickable {
                    onVote.invoke(choice.id)
                }
        }
    ) {
//        LinearProgressIndicator(
//            progress = animatedProgress,
//            color = Color.Yellow,
//            backgroundColor = Color.Transparent,
//            modifier = modifier
//                .background(
//                    brush = Brush.horizontalGradient(gradientColors),
//                    shape = RoundedCornerShape(16.dp)
//                )
//        )

//        var fillGageBarWidth = 0.dp
//        val density = LocalDensity.current
        Log.d("### $percent", "$choice")
        Box(
            modifier = Modifier.fillMaxWidth(
                if (!progress.isNon(progress)) {
                    progress
                } else 0.0f
            ).height(72.dp)
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = if (progress < 1.0) {
                        RoundedCornerShape(
                            topStart = 16.dp, bottomStart = 16.dp
                        )
                    } else {
                        RoundedCornerShape(16.dp)
                    }
                )
//                .onGloballyPositioned { layoutCoordinates ->
//                    fillGageBarWidth = with(density) { layoutCoordinates.size.width.toDp() }
//                }
//                .width(width = fillGageBarWidth * percent)
        )
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = indexText,
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
                        text = if (totalVoteCount > 0) {
                            "${(progress * 100).toInt()}%"
                        } else "0%",
                        color = Color.White, style = Font.montserratSemiBold14
                    )
                    Text(
                        text = "${choice.voteCount} 명",
                        color = MyColors.grey979797,
                        style = Font.montserratSemiBold12
                    )
                }
            }
        }
    }
}