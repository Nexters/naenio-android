package com.nexters.teamversus.naenio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nexters.teamversus.naenio.theme.Font
import com.nexters.teamversus.naenio.theme.MyColors

data class DialogModel(
    val title: String? = null,
    val message: String? = null,
    val button1Text: String? = null,
    val button2Text: String? = null,
    val button1Callback: (() -> Unit)? = null,
    val button2Callback: (() -> Unit)? = null,
)

@Composable
fun DialogContainer(
    dialogModel: DialogModel?,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    if (dialogModel == null) return
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        DialogComponent(dialogModel = dialogModel, onDismissRequest = onDismissRequest )
    }
}

@Composable
fun DialogComponent(
    dialogModel: DialogModel,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MyColors.darkGrey_313643, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        dialogModel.title?.let {
            Text(text = it, style = Font.pretendardSemiBold20, color = Color.White)
        }
        dialogModel.message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, style = Font.pretendardRegular16, color = Color.White)
        }

        Spacer(modifier = Modifier.height(33.dp))

        Row {
            dialogModel.button1Text?.let {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xff1e222c),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .weight(1f)
                        .clickable {
                            dialogModel.button1Callback?.invoke()
                            onDismissRequest.invoke()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = it,
                        style = Font.pretendardMedium16,
                        color = Color(0xff828282)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            dialogModel.button2Text?.let {
                Box(
                    modifier = Modifier
                        .background(
                            color = MyColors.blue_3979F2,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .weight(1f)
                        .clickable {
                            dialogModel.button2Callback?.invoke()
                            onDismissRequest.invoke()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = it,
                        style = Font.pretendardMedium16,
                        color = Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun DialogComponentPreview() {
    DialogComponent(
        dialogModel = DialogModel(
            title = "title",
            message = "message",
            button1Text = "확인",
            button2Text = "취소"
        )
    ){

    }
}