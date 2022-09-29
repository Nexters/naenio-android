package com.nexters.teamversus.naenio.utils.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

data class UserPref(
    val id: Int,
    val authServiceType: String,
    val nickname: String? = null,
    val profileImageIndex: Int = 0
)