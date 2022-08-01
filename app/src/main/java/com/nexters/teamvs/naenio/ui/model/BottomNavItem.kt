package com.nexters.teamvs.naenio.ui.model

import androidx.annotation.StringRes
import com.nexters.teamvs.naenio.R

sealed class BottomNavItem(
    @StringRes val titleRes: Int,
    val icon: Int,
    val route: String
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