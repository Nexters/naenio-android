package com.nexters.teamvs.naenio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.composables.*
import com.nexters.teamvs.naenio.utils.KeyboardUtils
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val keyboardUtils = KeyboardUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            var job: Job? = null
            var loadingState by remember { mutableStateOf(false) }
            var toastState by remember { mutableStateOf("") }
            var dialogState by remember { mutableStateOf<DialogModel?>(null) }

            LaunchedEffect(key1 = Unit) {
                GlobalUiEvent.uiEvent.collect {
                    when (it) {
                        UiEvent.ShowLoading -> {
                            loadingState = true
                        }
                        UiEvent.HideLoading -> {
                            loadingState = false
                        }
                        is UiEvent.ShowToast -> {
                            job?.cancel()
                            job = scope.launch {
                                toastState = it.message
                                delay(2000L)
                                toastState = ""
                            }
                        }
                        is UiEvent.ShowDialog -> {
                            dialogState = it.dialogModel
                        }
                        UiEvent.HideDialog -> {
                            dialogState = null
                        }
                        UiEvent.None -> {

                        }
                    }
                }
            }

            NaenioTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    RootNavigationGraph(navController = rememberNavController())
                    Loading(visible = loadingState)
                    Toast(
                        modifier = Modifier.align(Alignment.TopCenter),
                        message = "Message",
                        visible = toastState.isNotEmpty()
                    )
                    DialogContainer(dialogModel = dialogState)
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