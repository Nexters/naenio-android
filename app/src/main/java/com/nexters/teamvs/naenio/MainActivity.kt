package com.nexters.teamvs.naenio

import android.R.attr
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.RootNavigationGraph
import com.nexters.teamvs.naenio.theme.MyShape
import com.nexters.teamvs.naenio.theme.NaenioTheme
import com.nexters.teamvs.naenio.ui.component.*
import com.nexters.teamvs.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamvs.naenio.ui.dialog.SheetLayout
import com.nexters.teamvs.naenio.ui.tabs.BottomNavigationBar
import com.nexters.teamvs.naenio.utils.DeepLinkUtils
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


    @OptIn(ExperimentalMaterialApi::class)
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
            var menuDialogState by remember { mutableStateOf<List<MenuDialogModel>?>(null) }

            val modalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
            )
            var currentBottomSheet: CommentDialogModel? by remember { mutableStateOf(null) }

            if (!modalBottomSheetState.isVisible)
                currentBottomSheet = null

            val closeSheet: () -> Unit = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }

            val openSheet: (CommentDialogModel) -> Unit = {
                scope.launch {
                    currentBottomSheet = it
                    modalBottomSheetState.show()
                }
            }

            LaunchedEffect(key1 = Unit, block = {
                mainViewModel.navigateEvent.collect {
                    navController.navigate(it) {
                        launchSingleTop = true
                    }
                }
            })

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
                            menuDialogState = it.menuDialogModels
                        }
                        UiEvent.None -> {

                        }
                        UiEvent.ForceLogout -> {
                            mainViewModel.logout()
                            navController.navigate(AuthScreen.Login.route) {
                                popUpTo(Graph.MAIN) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }

            NaenioTheme {
                ModalBottomSheetLayout(
                    sheetState = modalBottomSheetState,
                    sheetShape = MyShape.TopRoundedCornerShape,
                    sheetContent = {
                        currentBottomSheet?.let { currentSheet ->
                            SheetLayout(
                                commentDialogModel = currentSheet,
                                onCloseBottomSheet = closeSheet,
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .background(Color.Transparent)
                        ) //content 비어있으면 error 발생으로 추가
                    }
                ) {
                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController = navController) }
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            RootNavigationGraph(
                                navController = navController,
                                startDestination = startDestination.value,
                                modalBottomSheetState = modalBottomSheetState,
                                openSheet = { openSheet.invoke(it) },
                                closeSheet = { closeSheet.invoke() }
                            )
                            Loading(visible = loadingState)
                            MenuDialog(
                                menuDialogModels = menuDialogState,
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        val postId = uri?.getQueryParameter("postId")
        mainViewModel.handleDeepLink(postId?.toInt())
        Log.d("++ onNewIntent", "${intent?.data}")
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