package com.nexters.teamvs.naenio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    menuDialogModels: List<MenuDialogModel>?,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    if (menuDialogModels.isNullOrEmpty()) return
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
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 27.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onDismissRequest.invoke()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                itemsIndexed(menuDialogModels) { index, model ->
                    MenuDialogItem(menuDialogModel = model, onDismissRequest = onDismissRequest)
                    if (menuDialogModels.size > 1 && index != menuDialogModels.size - 1) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xff424a5c))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuDialogItem(
    menuDialogModel: MenuDialogModel,
    onDismissRequest: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(62.dp)
            .clickable {
                menuDialogModel.onClick.invoke()
                onDismissRequest.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = menuDialogModel.text,
            textAlign = TextAlign.Center,
            color = menuDialogModel.color,
            style = Font.pretendardSemiBold18
        )
    }
}

@Preview
@Composable
fun MenuDialogPreview() {
    MenuDialog(
        listOf<MenuDialogModel>(
            MenuDialogModel(
                text = "Text1",
            ) {},
            MenuDialogModel(
                text = "Text2",
            ) {},
            MenuDialogModel(
                text = "Text2",
            ) {},
            MenuDialogModel(
                text = "Text2",
            ) {},
        ),
        onDismissRequest = {}
    )
}