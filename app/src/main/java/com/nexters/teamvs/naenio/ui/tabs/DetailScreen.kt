package com.nexters.teamvs.naenio.ui.tabs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexters.teamvs.naenio.theme.MyColors

@Composable
fun DetailScreen(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = name,
            fontSize = MaterialTheme.typography.h3.fontSize,
            fontWeight = FontWeight.Bold
        )
        BottomToolBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

val BottomBarHeight = 49.dp

@Composable
fun BottomToolBar(
    isBooth: Boolean = true,
    modifier: Modifier = Modifier,
    hasNotch: Boolean = false,
    showNextButton: Boolean = true,
    isChatMode: Boolean = false,
    isLoading: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(BottomBarHeight)
            .background(color = if (hasNotch) Color.Black else Color.Black.copy(alpha = 0.5f))
            .padding(end = 18.dp)
            .alpha(if (isLoading) 0.5f else 1f)
    ) {
        if (isBooth) {
            CharacterButton()

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(Color.Cyan),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                            .size(24.dp)
                    )
                }
                Text(
                    modifier = Modifier.background(Color.Blue),
                    text = "Background",
                    style = caption5,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .width(48.dp)
                .fillMaxHeight()
                .background(Color.Cyan),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
                Image(
                    painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .weight(1f)
                        .size(24.dp)
                )
            Text(
                modifier = Modifier.background(Color.Blue),
                text = "Background",
                style = caption5,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            if (showNextButton) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(112.dp, 36.dp)
                        .background(
                            MyColors.yellow_d9ff00,
                            RoundedCornerShape(8.dp)
                        )
                ) {
                    if (!isLoading) {
                        Text(
                            text = "아에이오우",
                            color = Color.White,
                            style = body2,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

val body2 = TextStyle(
    fontSize = 13.sp,
    lineHeight = 18.sp
)

@Composable
fun CharacterButton(
    memberCount: Int = 3,
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp)
            .width(48.dp)
            .background(Color.Yellow)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(24.dp)
                    .border(BorderStroke(2.dp, Color.White), shape = CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.TopCenter)
            )

            Text(
                text = "$memberCount",
                fontSize = 10.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp)
                    .size(13.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )
        }
        Text(
            modifier = Modifier.background(Color.Blue),
            text = "Member",
            style = caption5,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

val caption5 = TextStyle(
    fontSize = 10.sp,
    lineHeight = 14.sp
)