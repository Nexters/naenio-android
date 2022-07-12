package com.nexters.teamversus.naenio.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nexters.teamversus.naenio.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NaenioApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}