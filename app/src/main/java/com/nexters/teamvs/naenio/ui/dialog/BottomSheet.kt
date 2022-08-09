package com.nexters.teamvs.naenio.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.utils.KeyboardUtils
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.comment.CommentEvent
import com.nexters.teamvs.naenio.ui.comment.CommentScreen
import com.nexters.teamvs.naenio.ui.model.BaseComment

sealed class BottomSheetType {
    data class CommentType(
        val comments: List<BaseComment>,
        val onEvent: (CommentEvent) -> Unit,
    ) : BottomSheetType()

    object Menu : BottomSheetType()
}

@Composable
fun SheetLayout(
    currentScreen: BottomSheetType,
    onCloseBottomSheet: () -> Unit
) {
    val keyboardHeight = KeyboardUtils.keyboardHeight.collectAsState()

    BottomSheetContainer(onCloseBottomSheet) {
        when (currentScreen) {
            is BottomSheetType.CommentType -> {
                CommentScreen(
                    modifier = if (keyboardHeight.value <= 0) {
                        Modifier
                            .padding(bottom = 0.dp)
                            .fillMaxWidth()
                            .background(MyColors.darkGrey_313643, shape = RectangleShape)
                            .aspectRatio(0.6f)
                    } else {
                        Modifier
                            .padding(bottom = with(LocalDensity.current) { keyboardHeight.value.toDp() })
                            .fillMaxSize()
                    },
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
    Box(
        modifier
            .background(MyColors.darkGrey_313643)
            .fillMaxWidth()) {
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