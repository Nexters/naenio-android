package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.model.Comment

sealed class CommentEvent {
    data class Write(val text: String) : CommentEvent()
    data class Like(val like: Boolean) : CommentEvent()
    object More : CommentEvent()
}

@Composable
fun CommentSheetLayout(
    comments: List<Comment>,
    onEvent: (CommentEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MyColors.black75, shape = RectangleShape)
            .padding(24.dp)
            .aspectRatio(0.6f)
    ) {
        CommentHeader()
        Spacer(modifier = Modifier.height(24.dp))
        CommentList(
            modifier = Modifier.weight(1f),
            comments = comments,
            onEvent = onEvent
        )
        //TODO 키보드에 가리는 이슈,,
        CommentEditText(onEvent = onEvent)
    }

}

@Composable
fun CommentEditText(
    onEvent: (CommentEvent) -> Unit,
) {
    var input by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = MyColors.grey4d4d4d,
        ),
        value = input,
        trailingIcon = {
            Icon(
                modifier = Modifier.size(16.dp).clickable {
                    onEvent.invoke(CommentEvent.Write(input))
                },
                tint = Color.Yellow,
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "댓글 입력 버튼"
            )
        },
        shape = RoundedCornerShape(3.dp),
        onValueChange = {
            input = it
        }
    )
}

@Composable
fun CommentHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 9.dp),
            text = stringResource(id = R.string.comment),
            color = Color.White,
            fontSize = 21.sp,
        )

        Text(
            modifier = Modifier.padding(end = 9.dp),
            text = stringResource(id = R.string.comment),
            color = MyColors.grey20,
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "close"
        )
    }
}

@Composable
fun CommentList(
    modifier: Modifier,
    comments: List<Comment>,
    onEvent: (CommentEvent) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(comments) {
            CommentItem(it, onEvent)
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
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
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //TODO 프로필 이미지 타입 정의
            Icon(
                modifier = Modifier
                    .padding(end = 7.dp)
                    .size(21.dp),
                tint = Color.Yellow,
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "profileThumbnail"
            )
            Text(
                modifier = Modifier.wrapContentHeight().weight(1f),
                text = comment.writer.toString(),
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
                modifier = Modifier.size(16.dp).clickable {
                    onEvent.invoke(CommentEvent.More)
                },
                imageVector = Icons.Filled.MoreVert,
                tint = Color.White,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = comment.content, color = Color.White, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp)
                    .clickable {
                        onEvent.invoke(CommentEvent.Like(!comment.like))
                    }
                ,
                imageVector = if (comment.like) Icons.Filled.HeartBroken else Icons.Outlined.HeartBroken,
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = comment.likeCount.toString(),
                fontSize = 12.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(18.dp))

            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                imageVector = if (comment.like) Icons.Filled.Comment else Icons.Outlined.Comment,
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = comment.likeCount.toString(),
                fontSize = 12.sp,
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(MyColors.grey3f3f3f)
        )
    }
}

@Preview
@Composable
fun CommentSheetPreview() {
    CommentSheetLayout(
        comments = Comment.mock
    ) {

    }
}