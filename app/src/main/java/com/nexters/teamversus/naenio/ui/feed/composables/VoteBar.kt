package com.nexters.teamversus.naenio.ui.feed.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.domain.model.Choice
import com.nexters.teamversus.naenio.domain.model.Post
import com.nexters.teamversus.naenio.extensions.noRippleClickable
import com.nexters.teamversus.naenio.theme.Font
import com.nexters.teamversus.naenio.theme.MyColors
import com.nexters.teamversus.naenio.theme.NaenioTypography
import com.nexters.teamversus.naenio.ui.component.ProgressBar
import kotlinx.coroutines.delay

@Composable
fun VoteBar(
    post: Post,
    onVote: (Int, Int) -> Unit,
    maxLine: Int = 2
) {
    /** 둘 중 하나라도 투표를 했는 지 */
    val isVotedForPost = post.isAlreadyVote

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
                },
                maxLine = maxLine
            )
            Spacer(modifier = Modifier.height(18.dp))
            GageBar(
                indexText = "B.",
                choice = post.choice2,
                isVotedForPost = isVotedForPost,
                totalVoteCount = post.totalVoteCount,
                onVote = { voteId ->
                    onVote.invoke(post.id, voteId)
                },
                maxLine = maxLine
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
    choice: Choice,
    indexText: String,
    totalVoteCount: Int,
    isVotedForPost: Boolean,
    onVote: (Int) -> Unit,
    maxLine: Int = 4
) {
    val isMyChoice = choice.isVoted

    val gradientColors = listOf(
        MyColors.purple_d669ff, MyColors.blue_5862ff, MyColors.blue_0bb9ff
    )

    val animatable = isVotedForPost
    val targetValue = (choice.voteCount.toFloat() / totalVoteCount.toFloat()) * 100
    var progressValue by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = animatable, key2 = targetValue) {
        if (!isVotedForPost) return@LaunchedEffect
        if (progressValue < targetValue) {
            while ((progressValue < targetValue) && animatable) {
                progressValue++
                delay(1)
            }
        } else {
            while ((progressValue > targetValue) && animatable) {
                progressValue--
                delay(1)
            }
        }
        progressValue = targetValue.toInt()
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
    ) {
        ProgressBar(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    border = if (isMyChoice) BorderStroke(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            gradientColors
                        ),
                    ) else BorderStroke(0.dp, Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                )
                .noRippleClickable {
                    onVote.invoke(choice.id)
                },
            progressValue = if (animatable) progressValue else 0,
        )
        VoteBarContent(
            modifier = Modifier.align(Alignment.Center),
            indexText = indexText,
            choice = choice,
            isVotedForPost = isVotedForPost,
            percentageValue = progressValue,
            maxLine = maxLine
        )
    }
}

@Composable
fun VoteBarContent(
    modifier: Modifier,
    indexText: String,
    choice: Choice,
    isVotedForPost: Boolean,
    percentageValue: Int,
    maxLine: Int = 2
) {
    Row(
        modifier = modifier
            .wrapContentSize()
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
            maxLines = maxLine,
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
                    text = "${percentageValue}%",
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

@Preview
@Composable
fun GageBarPreview() {

}