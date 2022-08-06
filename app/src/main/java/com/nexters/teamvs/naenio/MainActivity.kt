package com.nexters.teamvs.naenio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import com.nexters.teamvs.naenio.utils.datastore.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NaenioTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }

        Log.d("### user token " , AuthDataStore.authToken)
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var deepLink : Uri? = null
                if(pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.d("###", "dynamicLink 수신 테스트 :: ${deepLink.toString()}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("###", "dynamicLink 수신 에러 :: $e")
            }
    }
}