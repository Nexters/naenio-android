package com.nexters.teamvs.naenio.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.nexters.teamvs.naenio.theme.Font
 //TODO 팝업 윈도우로 바꿔보기
@Composable
fun Toast(
    modifier: Modifier = Modifier,
    message: String,
    visible: Boolean = false,
) {
    if (!visible) return
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                modifier = modifier
                    .zIndex(1f)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color(0xff313643),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xffd669ff),
                                    Color(0xff5862ff),
                                    Color(0xff0bb9ff)
                                )
                            )
                        )
                    )
                    .padding(16.dp),
                text = message,
                textAlign = TextAlign.Center,
                style = Font.pretendardRegular16,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ToastPreview() {
    Toast(message = "123123")
}