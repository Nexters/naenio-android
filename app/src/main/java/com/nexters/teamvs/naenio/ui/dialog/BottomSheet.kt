package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .background(Color.Yellow, shape = RectangleShape)
    ) {
        Text(
            text = "This is CommentSheetLayout",
            Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            color = Color.Black,
            fontSize = 15.sp
        )
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