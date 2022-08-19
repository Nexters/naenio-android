package com.nexters.teamvs.naenio.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.extensions.noRippleClickable

@Composable
fun Loading(
    visible: Boolean,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.full_loading))

    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .noRippleClickable { },
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                iterations = Int.MAX_VALUE
            )
        }
    }
}