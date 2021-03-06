package com.nexters.teamvs.naenio.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.nexters.teamvs.naenio.BuildConfig
import com.nexters.teamvs.naenio.utils.datastore.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NaenioApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        DataStoreUtils.init(context)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        FirebaseApp.initializeApp(context)
    }
}