package com.nexters.teamvs.naenio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.MyColors.Companion.red_ff1f1f

data class MenuDialogModel(
    val text: String,
    val color: Color = red_ff1f1f,
    val onClick: () -> Unit,
)

@Composable
fun MenuDialog(
    menuDialogModel: MenuDialogModel?,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    if (menuDialogModel == null) return
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .noRippleClickable { onDismissRequest.invoke() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 27.dp)
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(color = MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        menuDialogModel.onClick.invoke()
                        onDismissRequest.invoke()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = menuDialogModel.text,
                    color = menuDialogModel.color,
                    style = Font.pretendardSemiBold18
                )
            }
        }
    }
}