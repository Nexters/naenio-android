package com.nexters.teamvs.naenio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TestButton(
    text: String = "Test",
    onClick: () -> Unit,
) {
    Text(text = text, modifier = Modifier
        .size(200.dp)
        .background(Color.Blue)
        .clickable { onClick.invoke() }
    )
}