package com.nexters.teamvs.naenio.ui.model

import androidx.annotation.StringRes
import com.nexters.teamvs.naenio.R

sealed class BottomNavItem(
    @StringRes val titleRes: Int,
    val icon: Int,
    val route: String
) {
    object Theme : BottomNavItem(
        R.string.bottom_item_theme,
        R.drawable.ic_theme,
        "theme"
    )

    object Feed : BottomNavItem(
        R.string.bottom_item_feed,
        R.drawable.ic_home,
        "feed"
    )

    object Profile : BottomNavItem(
        R.string.bottom_item_profile,
        R.drawable.ic_profile,
        "profile"
    )
}