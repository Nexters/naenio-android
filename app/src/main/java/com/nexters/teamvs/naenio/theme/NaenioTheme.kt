package com.nexters.teamvs.naenio.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun NaenioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = NaenioTypography,
        content = content
    )
}

val DarkColors = darkColors(
    primary = MyColors.yellow_d9ff00,
    primaryVariant = MyColors.yellow_d9ff00,
    onPrimary = Color.Black,
    secondary = MyColors.yellow_d9ff00,
    onSecondary = Color.Black,
    error = Color.Red
)

val LightColors = lightColors(
    primary = MyColors.yellow_d9ff00,
    primaryVariant = Color.Yellow,
    onPrimary = Color.White,
    secondary = MyColors.yellow_d9ff00,
    secondaryVariant = MyColors.yellow_d9ff00,
    onSecondary = Color.White,
    error = Color.Red
)