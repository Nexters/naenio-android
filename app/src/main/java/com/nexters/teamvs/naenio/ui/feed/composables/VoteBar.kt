package com.nexters.teamvs.naenio.ui.feed.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
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
                modifier = gageBarModifier,
                gageModifier = fillGageBarModifier,
                foregroundColor = Brush.horizontalGradient(
                    gageBarColorList
                ),
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
    modifier: Modifier,
    choice: Choice,
    indexText: String,
    totalVoteCount: Int,
    gageModifier: Modifier,
    foregroundColor: Brush,
    isVotedForPost: Boolean,
    onVote: (Int) -> Unit
) {
    val isMyChoice = choice.isVoted

    Box(
        modifier = if (isMyChoice) {
            modifier
                .border(
                    border = BorderStroke(1.dp, foregroundColor),
                    shape = RoundedCornerShape(16.dp)
                )
                .noRippleClickable {
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
                            "${((choice.voteCount.toFloat() / totalVoteCount.toFloat()) * 100).toInt()}%"
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