package com.nexters.teamvs.naenio

import android.R.attr
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.component.*
import com.nexters.teamvs.naenio.utils.*
import com.nexters.teamvs.naenio.utils.keyboard.KeyboardAwareWindow
import com.nexters.teamvs.naenio.utils.keyboard.KeyboardHeightObserver
import com.nexters.teamvs.naenio.utils.keyboard.KeyboardHeightProvider
import com.nexters.teamvs.naenio.utils.keyboard.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    KeyboardHeightObserver {

    private val keyboardUtils by lazy { KeyboardUtils() }

    private val mainViewModel: MainViewModel by viewModels()
    private var keyboardHeightProvider: KeyboardHeightProvider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeepLinkUtils.setDeepLinkListener(intent = intent)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        installSplashScreen()
            .setKeepOnScreenCondition { !mainViewModel.isReady }


        val heightLiveData = MutableLiveData(0)


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

//        keyboardUtils.setKeyboardListener(window.decorView)

        keyboardHeightProvider =
            KeyboardHeightProvider(this)

        // make sure to start the keyboard height provider after the onResume
        // of this activity. This is because a popup window must be initialised
        // and attached to the activity root view.

        // make sure to start the keyboard height provider after the onResume
        // of this activity. This is because a popup window must be initialised
        // and attached to the activity root view.
        val view = window.decorView
        view.post { keyboardHeightProvider!!.start() }


        val window2 = KeyboardAwareWindow(this).apply {
            onKeyboardChangedEvent = { height: Int, _: Int ->
                heightLiveData.postValue(height)
            }
        }
        this.lifecycle.addObserver(window2)

        heightLiveData.observe(this) {
            Log.d("### keyboardUitls2 ", "$it")
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onPause() {
        super.onPause()
        keyboardHeightProvider?.setKeyboardHeightObserver(null)
    }

    /**
     * {@inheritDoc}
     */
    override fun onResume() {
        super.onResume()
        keyboardHeightProvider?.setKeyboardHeightObserver(this)
    }

    /**
     * {@inheritDoc}
     */
    override fun onDestroy() {
        super.onDestroy()
        keyboardHeightProvider?.close()
    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        val orientationLabel =
            if (attr.orientation == Configuration.ORIENTATION_PORTRAIT) "portrait" else "landscape"
        Log.i("TAG", "onKeyboardHeightChanged in pixels: $height $orientationLabel")

        KeyboardUtils.setKeyboardHeight(heightPx = height)
    }
}