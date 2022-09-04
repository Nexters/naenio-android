package com.nexters.teamvs.naenio.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

object DeepLinkUtils {

    fun setDeepLinkListener(intent: Intent) {
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                val deepLink: Uri?
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    deepLink?.let {
                        Log.d("### dynamic link", "dynamicLink 수신 테스트 :: ${it.host}")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("### dynamic link", "dynamicLink 수신 에러 :: $e")
            }
    }
}