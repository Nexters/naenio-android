package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.ui.model.Comment

sealed class BottomSheetType {
    data class CommentType(
        val comments: List<Comment>,
        val onEvent: (CommentEvent) -> Unit,
    ) : BottomSheetType()

    object Menu : BottomSheetType()
}

@Composable
fun SheetLayout(
    currentScreen: BottomSheetType,
    onCloseBottomSheet: () -> Unit
) {
    BottomSheetContainer(onCloseBottomSheet) {
        when (currentScreen) {
            is BottomSheetType.CommentType -> {
                CommentSheetLayout(
                    comments = currentScreen.comments,
                    onEvent = currentScreen.onEvent
                )
            }
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