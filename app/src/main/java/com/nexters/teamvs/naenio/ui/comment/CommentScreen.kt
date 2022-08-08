package com.nexters.teamvs.naenio.ui.comment

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.Font.pretendardRegular14
import com.nexters.teamvs.naenio.theme.Font.pretendardSemiBold14
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.model.BaseComment
import com.nexters.teamvs.naenio.ui.model.Comment

sealed class CommentEvent {
    data class Write(val text: String) : CommentEvent()
    data class Like(val like: Boolean) : CommentEvent()
    object More : CommentEvent()
    object Close : CommentEvent()
}

sealed class CommentMode {
    object COMMENT : CommentMode()
    data class REPLY(val parentComment: Comment) : CommentMode()
}

@Composable
fun CommentScreen(
    modifier: Modifier = Modifier,
    postId: Int,
    onEvent: (CommentEvent) -> Unit,
) {
    /**
     * 댓글 창을 보여줘야 하면 true. 답글 창을 보여줘야 하면 false
     */
    var mode by remember { mutableStateOf<CommentMode>(CommentMode.COMMENT) }

    AnimatedVisibility(
        visible = mode == CommentMode.COMMENT,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        CommentSheetLayout(
            modifier = modifier,
            commentViewModel = hiltViewModel(),
            postId = postId,
            changeMode = {
                mode = it
            },
            onEvent = onEvent
        )
    }

    AnimatedVisibility(
        visible = mode is CommentMode.REPLY,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        ReplySheetLayout(
            modifier = modifier,
            replyViewModel = hiltViewModel(),
            postId = postId,
            parentComment = (mode as? CommentMode.REPLY)?.parentComment
                ?: return@AnimatedVisibility,
            changeMode = {
                mode = it
            },
            onEvent = onEvent
        )
    }
}

@Composable
fun CommentSheetLayout(
    modifier: Modifier,
    commentViewModel: CommentViewModel,
    postId: Int,
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    val comments = commentViewModel.comments.collectAsState()
    Column(
        modifier = modifier
    ) {
        CommentHeader(
            commentCount = comments.value.size,
            onEvent = onEvent
        )
        CommentList(
            modifier = Modifier.weight(1f),
            mode = CommentMode.COMMENT,
            comments = comments.value,
            changeMode = changeMode,
            onEvent = onEvent
        )
        CommentEditText(onEvent = onEvent)
    }
}

@OptIn(ExperimentalAnimatedInsets::class)
@Composable
fun CommentEditText(
    onEvent: (CommentEvent) -> Unit,
) {
    var input by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .background(Color(0xff20232c))
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        ProfileImageIcon(Modifier.padding(top = 16.dp))

        TextField(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .padding(vertical = 15.dp)
                .weight(1f)
                .defaultMinSize(minHeight = 32.dp),
            textStyle = pretendardRegular14,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color(0xff4F5564),
            ),
            value = input,
            shape = RoundedCornerShape(3.dp),
            onValueChange = {
                input = it
            }
        )

        Text(
            style = pretendardSemiBold14,
            color = MyColors.pink,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Bottom)
                .padding(start = 12.dp, bottom = 16.dp)
                .clickable {
                    onEvent.invoke(CommentEvent.Write(input))
                },
            text = stringResource(id = R.string.write_comment)
        )
    }
}

@Composable
fun CommentHeader(
    commentCount: Int,
    onEvent: (CommentEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_comment_icon),
            contentDescription = "heard comment icon"
        )
        Text(
            modifier = Modifier.padding(start = 6.dp, end = 9.dp),
            text = stringResource(id = R.string.comment),
            color = Color.White,
            style = Font.pretendardSemiBold16
        )

        Text(
            modifier = Modifier.padding(end = 9.dp),
            text = commentCount.toString(),
            color = MyColors.grey_d9d9d9,
            style = Font.pretendardRegular16
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.clickable {
                onEvent.invoke(CommentEvent.Close)
            },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close"
        )
    }
}

@Composable
fun CommentList(
    modifier: Modifier,
    mode: CommentMode,
    comments: List<BaseComment>,
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 19.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MyColors.grey3f3f3f)
            )
        }
        items(comments) {
            CommentItem(
                comment = it,
                mode = mode,
                onCommentMode = changeMode,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CommentItem(
    comment: BaseComment,
    mode: CommentMode,
    onCommentMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (comment is Comment) 20.dp else 52.dp, end = 20.dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImageIcon()
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = comment.writer.nickname ?: "???",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(end = 6.dp),
                text = comment.writeTime.toString(),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1
            )
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .clickable {
                        onEvent.invoke(CommentEvent.More)
                    },
                painter = painterResource(id = R.drawable.ic_more),
                tint = Color.White,
                contentDescription = "more"
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Column(
            modifier = Modifier.padding(
                start = if (comment is Comment) 52.dp else 83.dp,
                end = 20.dp
            )
        ) {
            Text(text = comment.content, color = Color.White, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(12.dp)
                        .clickable {
                            onEvent.invoke(CommentEvent.Like(!comment.isLiked))
                        },
                    colorFilter = ColorFilter.tint(color = if (comment.isLiked) Color.Red else Color.White),
                    painter = painterResource(id = R.drawable.ic_heart_outlined),
                    contentDescription = null
                )
                Text(
                    text = comment.likeCount.toString(),
                    fontSize = 12.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.width(18.dp))

                if (comment is Comment) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(12.dp),
                        painter = painterResource(id = R.drawable.ic_comment),
                        tint = Color.White,
                        contentDescription = null
                    )
                    Text(
                        text = comment.likeCount.toString(),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }

            if (mode == CommentMode.COMMENT) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.clickable {
                        onCommentMode.invoke(CommentMode.REPLY(comment as Comment))
                    },
                    text = stringResource(id = R.string.see_replies),
                    fontSize = 16.sp,
                    color = MyColors.blue_3979F2
                )
            }
        }
        Spacer(
            modifier = Modifier
                .padding(vertical = 19.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(MyColors.grey3f3f3f)
        )
    }
}

@Composable
fun ProfileImageIcon(modifier: Modifier = Modifier) {
    //TODO 프로필 이미지 타입 정의
    Icon(
        modifier = modifier
            .padding(end = 8.dp)
            .size(21.dp)
            .clip(CircleShape),
        tint = Color.Yellow,
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "profileThumbnail"
    )
}

@Preview
@Composable
fun CommentSheetPreview() {
    CommentSheetLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(MyColors.darkGrey_313643, shape = RectangleShape)
            .aspectRatio(0.6f),
        postId = -1,
        commentViewModel = viewModel(),
        changeMode = {}
    ) {

    }
}