package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.tabs.model.CommentUiModel

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
        Icon(imageVector = Icons.Filled.Image, contentDescription = "profile")
        Text(text = "댓글", color = Color.White)
    }
}