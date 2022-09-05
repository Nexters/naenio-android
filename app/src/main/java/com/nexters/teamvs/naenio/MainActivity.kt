package com.nexters.teamvs.naenio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.component.*
import com.nexters.teamvs.naenio.utils.DeepLinkUtils
import com.nexters.teamvs.naenio.utils.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val keyboardUtils by lazy { KeyboardUtils() }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeepLinkUtils.setDeepLinkListener(intent = intent)

        installSplashScreen()
            .setKeepOnScreenCondition { !mainViewModel.isReady }

        setContent {
            val navController = rememberNavController()
            val startDestination = mainViewModel.startDestination.collectAsState()
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
                        UiEvent.ForceLogout -> {
                            mainViewModel.logout()
                            navController.clearBackStack(Graph.MAIN)
                            navController.navigate(AuthScreen.Login.route)
                        }
                    }
                }
            }

            NaenioTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    RootNavigationGraph(
                        navController = navController,
                        startDestination = startDestination.value
                    )
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
    }
}