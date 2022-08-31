package com.nexters.teamvs.naenio

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.component.*
import com.nexters.teamvs.naenio.utils.KeyboardUtils
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val keyboardUtils = KeyboardUtils()
    var isSplashTimeout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        val splashTimer = Timer()
        splashTimer.schedule(object: TimerTask() {
            override fun run() {
                isSplashTimeout = true
            }
        },3000)
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                return if (isSplashTimeout) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                } else false
            }
        })
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    deepLink?.let {
                        Log.d("### dynamic link", "dynamicLink 수신 테스트 :: ${it.toString()}")
                        Log.d("### dynamic link", "dynamicLink 수신 테스트 :: ${it.host}")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("### dynamic link", "dynamicLink 수신 에러 :: $e")
            }

        setContent {
            val scope = rememberCoroutineScope()
            var job: Job? = null
            var loadingState by remember { mutableStateOf(false) }
            var toastState by remember { mutableStateOf("") }
            var dialogState by remember { mutableStateOf<DialogModel?>(null) }
            var menuDialogState by remember { mutableStateOf<MenuDialogModel?>(null) }

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
                        UiEvent.HideMenuDialog -> {
                            menuDialogState = null
                        }
                        is UiEvent.ShowMenuDialog -> {
                            menuDialogState = it.menuDialogModel
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
                    MenuDialog(
                        menuDialogModel = menuDialogState,
                        onDismissRequest = {
                            menuDialogState = null
                        },
                    )
                    DialogContainer(
                        dialogModel = dialogState,
                        onDismissRequest = {
                            dialogState = null
                        }
                    )
                    Toast(
                        modifier = Modifier.align(Alignment.TopCenter),
                        message = toastState,
                        visible = toastState.isNotEmpty()
                    )
                }
            }
        }

        keyboardUtils.setKeyboardListener(window.decorView)

        Log.d("### user token ", AuthDataStore.authToken)
    }
}