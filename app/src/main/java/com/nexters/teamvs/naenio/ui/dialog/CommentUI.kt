package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.model.CommentUiModel

sealed class CommentEvent {
    data class Write(val commentUiModel: CommentUiModel) : CommentEvent()
    data class Like(val like: Boolean) : CommentEvent()
    object More : CommentEvent()
}

@Composable
fun CommentSheetLayout(
    commentUiModels: List<CommentUiModel>,
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
        CommentList(commentUiModels, onEvent)
    }
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
    commentUiModels: List<CommentUiModel>,
    onEvent: (CommentEvent) -> Unit,
) {
    LazyColumn {
        items(commentUiModels) {
            CommentItem(it, onEvent)
        }
    }
}

@Composable
fun CommentItem(
    commentUiModel: CommentUiModel,
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
                text = commentUiModel.userId.toString(),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(end = 6.dp),
                text = commentUiModel.time.toString(),
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1
            )
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Filled.MoreVert,
                tint = Color.White,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = commentUiModel.content, color = Color.White, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                imageVector = if (commentUiModel.like) Icons.Filled.HeartBroken else Icons.Outlined.HeartBroken,
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = commentUiModel.likeCount.toString(),
                fontSize = 12.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.width(18.dp))

            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                imageVector = if (commentUiModel.like) Icons.Filled.Comment else Icons.Outlined.Comment,
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = commentUiModel.likeCount.toString(),
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
        commentUiModels = CommentUiModel.mock
    ) {

    }
}