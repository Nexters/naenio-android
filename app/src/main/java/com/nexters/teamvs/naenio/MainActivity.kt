package com.nexters.teamvs.naenio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.composables.Loading
import com.nexters.teamvs.naenio.utils.KeyboardUtils
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val keyboardUtils = KeyboardUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val visibleLoading = GlobalUiEvent.uiEvent.collectAsState(initial = UiEvent.None)
            var loadingState by remember { mutableStateOf(false) }

            when (visibleLoading.value) {
                UiEvent.ShowLoading -> {
                    loadingState = true
                }
                UiEvent.HideLoading -> {
                    loadingState = false
                }
                else -> {}
            }

            NaenioTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    RootNavigationGraph(navController = rememberNavController())
                    Loading(visible = loadingState)
//                    Toast(
//                        modifier = Modifier.align(Alignment.TopCenter),
//                        message = "Message",
//                        visible = true
//                    )
                }
            }
        }

        keyboardUtils.setKeyboardListener(window.decorView)

        Log.d("### user token ", AuthDataStore.authToken)

        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.d("###", "dynamicLink 수신 테스트 :: ${deepLink.toString()}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("###", "dynamicLink 수신 에러 :: $e")
            }
    }
}