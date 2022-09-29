package com.nexters.teamversus.naenio.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.nexters.teamversus.naenio.BuildConfig
import com.nexters.teamversus.naenio.utils.DimensionUtils
import com.nexters.teamversus.naenio.utils.datastore.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NaenioApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        var isShortScreen: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        DataStoreUtils.init(context)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        FirebaseApp.initializeApp(context)
        isShortScreen = DimensionUtils.checkShortDevice().also {
            Log.d("### isShortScreen", it.toString())
        }
    }
}