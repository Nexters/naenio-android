package com.nexters.teamvs.naenio.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.nexters.teamvs.naenio.R

import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeedScreen(navController: NavHostController, modifier: Modifier) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        FeedPager(Modifier.padding(padding))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeedPager(modifier: Modifier = Modifier) {
    VerticalPager(
        count = 10,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = modifier
            .padding(bottom = 60.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ) { page ->
        Box(
            Modifier
                .padding(top = 20.dp)
                .graphicsLayer {
//                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
//
//                    lerp(
//                        start = 0.85f,
//                        stop = 1f,
//                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                    ).also { scale ->
//                        scaleX = scale
//                        scaleY = scale
//                    }
//
//                    alpha = lerp(
//                        start = 0.5f,
//                        stop = 1f,
//                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                    )
                }
        ) {
            FeedItem()
        }
    }
}

@Composable
fun FeedItem() {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Feed Screen",
            color = Color.White,
            fontSize = 17.sp
        )
        GageBar(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .height(59.dp),
            gageModifier = Modifier.height(59.dp),
            width = 300.dp,
            backgroundColor = Color.Gray,
            foregroundColor = Brush.horizontalGradient(
                listOf(
                    Color(0xffFD7D20),
                    Color(0xffFBE41A)
                )
            ),
            percent = gage,
        )
    }
}

@Composable
fun GageBar(
    modifier: Modifier,
    gageModifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Float,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(width)
    ) {
        Box(
            modifier = gageModifier
                .background(foregroundColor)
                .width(width * percent)
        )
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    FeedScreen(
        navController = NavHostController(LocalContext.current),
        modifier = Modifier.padding(bottomBarHeight)
    )
}