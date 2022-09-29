package com.nexters.teamversus.naenio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexters.teamversus.naenio.theme.MyColors

@Composable
fun ProgressBar(
    modifier: Modifier,
    progressValue: Int,
) {
    val gradientColors = listOf(MyColors.purple_d669ff, MyColors.blue_5862ff, MyColors.blue_0bb9ff)

    Card(
        modifier = modifier,
        backgroundColor = Color.Black,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressValue.toFloat() / 100f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(gradientColors),
                        shape = if (progressValue < 100) {
                            RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                        } else {
                            RoundedCornerShape(16.dp)
                        }
                    ),
            )
        }
    }
}