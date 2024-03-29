package com.nexters.teamversus.naenio.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamversus.naenio.MainViewModel.Companion.deepLinkPostId

object DeepLinkUtils {

    const val deepLink = "https://naenio.shop"
    const val shortDynamicLink = "https://naenioapp/test2"

    fun setDeepLinkListener(intent: Intent) {
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                val deepLink: Uri?
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    deepLink?.let {
                        Log.d("++ dynamic link", "dynamicLink 수신 테스트 :: ${it.toString()}")
                        kotlin.runCatching {
                            deepLinkPostId = it.getQueryParameter("postId")?.toInt()
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("++ dynamic link", "dynamicLink 수신 에러 :: $e")
            }
    }
}