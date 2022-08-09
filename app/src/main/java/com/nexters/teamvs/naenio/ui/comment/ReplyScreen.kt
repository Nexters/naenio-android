package com.nexters.teamvs.naenio.ui.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.data.network.dto.CommentParentType
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors

@Composable
fun ReplySheetLayout(
    modifier: Modifier,
    replyViewModel: ReplyViewModel,
    parentComment: Comment,
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    val replies = replyViewModel.replies.collectAsState()
    Column(
        modifier = modifier
    ) {
        ReplyHeader(changeMode = changeMode, onEvent = onEvent)
        ReplyList(
            modifier = Modifier.weight(1f),
            parentComment = parentComment,
            comments = replies.value,
            mode = CommentMode.REPLY(parentComment),
            changeMode = changeMode,
            onEvent = onEvent
        )
        ReplyInput(
            commentId = parentComment.id,
            onEvent = onEvent
        )
    }
}

@Composable
fun ReplyHeader(
    changeMode: (CommentMode) -> Unit,
    onEvent: (CommentEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.clickable {
                changeMode.invoke(CommentMode.COMMENT)
            },
            painter = painterResource(id = R.drawable.ic_back_left),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(start = 5.dp, end = 9.dp),
            text = stringResource(id = R.string.reply_comment),
            color = Color.White,
            style = Font.pretendardSemiBold16
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
fun ReplyList(
    modifier: Modifier,
    parentComment: Comment,
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
        item {
            CommentItem(
                comment = parentComment,
                mode = mode,
                onCommentMode = changeMode,
                onEvent = onEvent
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
fun ReplyInput(
    commentId: Int,
    onEvent: (CommentEvent) -> Unit
) {
    CommentEditText {
        onEvent.invoke(
            CommentEvent.Write(
                parentId = commentId,
                parentType = CommentParentType.COMMENT,
                content = it
            )
        )
    }
}