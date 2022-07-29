package com.nexters.teamvs.naenio.ui.tabs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors

import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

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
                .background(
                    when (page) {
                        0 -> Color.Yellow
                        1 -> Color.Red
                        2 -> Color.Cyan
                        else -> Color.Magenta
                    }
                )
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            GageBar()
        }
    }
}

@Composable
fun GageBar() {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })
    var enabled by remember { mutableStateOf(false) }

    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
    )

    LaunchedEffect(enabled) {
        while ((progress < 1)) {
            progress += 0.05f
            delay(10)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Feed Screen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        CustomProgressBar(
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
            isShownText = true
        )

        if (progress >= 1f) {
            enabled = false
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = animatedProgress,
                color = MyColors.PrimaryColor,
                backgroundColor = Color.Black,
                modifier = Modifier
                    .requiredHeight(59.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun CustomProgressBar(
    modifier: Modifier,
    gageModifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Float,
    isShownText: Boolean
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