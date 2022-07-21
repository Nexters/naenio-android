package com.nexters.teamversus.naenio.ui.tabs.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.nexters.teamversus.naenio.R

sealed class BottomNavItem(
    @StringRes val titleRes: Int,
    val icon: Int,
    val screenRoute: String
) {
    object Home : BottomNavItem(
        R.string.bottom_item_home,
        R.drawable.ic_launcher_background,
        "home"
    )

    object Feed : BottomNavItem(
        R.string.bottom_item_feed,
        R.drawable.ic_launcher_background,
        "feed"
    )

    object Profile : BottomNavItem(
        R.string.bottom_item_profile,
        R.drawable.ic_launcher_background,
        "profile"
    )
}