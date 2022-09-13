package com.nexters.teamvs.naenio.utils

import android.util.Log
import com.nexters.teamvs.naenio.base.NaenioApp
import kotlin.properties.Delegates

object DimensionUtils {

    fun checkShortDevice(): Boolean {
        val displayMetrics = NaenioApp.context.resources.displayMetrics.also {
            Log.d("### Device Size", "${it.widthPixels} x ${it.heightPixels}")
        }
        val radio = displayMetrics.heightPixels.toFloat() / displayMetrics.widthPixels.toFloat()
        return radio < 16f/9f
    }
}