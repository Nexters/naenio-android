package com.nexters.teamvs.naenio.ui.tabs.auth.model

import androidx.annotation.DrawableRes
import com.nexters.teamvs.naenio.R

data class ProfileImageModel(
    val id: Int,
    @DrawableRes val image: Int,
) {
    companion object {
        val mock = ProfileImageModel(id = 0, image = R.drawable.ic_profile)
    }
}

object Profile {
    val images = listOf(
        ProfileImageModel(
            id = 0,
            image = R.drawable.profile_cat_1
        ),
        ProfileImageModel(
            id = 1,
            image = R.drawable.profile_cat_2
        ),
        ProfileImageModel(
            id = 2,
            image = R.drawable.profile_cat_3
        ),

        ProfileImageModel(
            id = 3,
            image = R.drawable.profile_dog_1
        ),
        ProfileImageModel(
            id = 4,
            image = R.drawable.profile_dog_2
        ),
        ProfileImageModel(
            id = 5,
            image = R.drawable.profile_dog_3
        ),

        ProfileImageModel(
            id = 6,
            image = R.drawable.profile_rabbit_1
        ),
        ProfileImageModel(
            id = 7,
            image = R.drawable.profile_rabbit_2
        ),
        ProfileImageModel(
            id = 8,
            image = R.drawable.profile_rabbit_3
        ),
    )

}