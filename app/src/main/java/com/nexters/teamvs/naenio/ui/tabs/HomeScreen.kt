package com.nexters.teamvs.naenio.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.theme.MyColors

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
            .wrapContentSize(Alignment.Center)
    ) {
        LottieAnimation(
            composition,
            modifier = Modifier.wrapContentSize(),
            iterations = Int.MAX_VALUE
        )
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Text(
            text = "Go to LoginScreen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                navController.navigate(Graph.AUTHENTICATION)
            },
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Text(
            text = "Go to DetailScreen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                navController.navigate(Graph.DETAILS)
            },
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}