package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.theme.MyColors

sealed class BottomSheetType {
    object Comment : BottomSheetType()
    object Menu : BottomSheetType()
}

@Composable
fun SheetLayout(currentScreen: BottomSheetType, onCloseBottomSheet: () -> Unit) {
    BottomSheetContainer(onCloseBottomSheet) {
        when (currentScreen) {
            BottomSheetType.Comment -> CommentSheetLayout()
            BottomSheetType.Menu -> MenuSheetLayout()
        }
    }
}

@Composable
fun BottomSheetContainer(
    onClosePressed: () -> Unit,
    modifier: Modifier = Modifier,
    closeButtonColor: Color = Color.Gray,
    content: @Composable() () -> Unit
) {
    Box(modifier.fillMaxWidth()) {
        content()
        IconButton(
            onClick = onClosePressed,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(29.dp)
        ) {
            Icon(Icons.Filled.Close, tint = closeButtonColor, contentDescription = null)
        }

    }
}

@Composable
fun CommentSheetLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MyColors.black75, shape = RectangleShape)
            .padding(24.dp)
            .aspectRatio(0.6f)
    ) {
        CommentHeader()
        CommentList()
    }
}

@Composable
fun CommentHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 9.dp),
            text = stringResource(id = com.nexters.teamvs.naenio.R.string.comment),
            color = Color.White,
            fontSize = 21.sp,
        )

        Text(
            modifier = Modifier.padding(end = 9.dp),
            text = stringResource(id = com.nexters.teamvs.naenio.R.string.comment),
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
fun CommentList() {
    LazyColumn {
        items(5) {
            CommentItem()
        }
    }
}

@Composable
fun CommentItem() {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Icon(imageVector = Icons.Filled.Image, contentDescription = "profile")
        Text(text = "댓글", color = Color.White)
    }
}

@Composable
fun MenuSheetLayout() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan, shape = RectangleShape)
    ) {
        Text(
            text = "This is MenuSheetLayout",
            Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            color = Color.Black,
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
fun MenuSheetPreview() {
    SheetLayout(currentScreen = BottomSheetType.Menu) {

    }
}

@Preview
@Composable
fun CommentSheetPreview() {
    SheetLayout(currentScreen = BottomSheetType.Comment) {

    }
}