package com.nexters.teamvs.naenio.utils.keyboard

import android.graphics.Rect
import android.inputmethodservice.Keyboard
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.flow.MutableStateFlow

class KeyboardUtils {

    companion object {
        val keyboardHeight = MutableStateFlow(0)
        fun setKeyboardHeight(heightPx: Int) {
            keyboardHeight.tryEmit(heightPx)
        }
    }

    fun setKeyboardListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).bottom

            val keyboardHeight = imeHeight - navigationBarHeight
            Log.d("### Keyboard State", "$imeHeight $statusBarHeight $navigationBarHeight")

            if (imeVisible && keyboardHeight > 0) {
                setKeyboardHeight(keyboardHeight)
            } else {
                setKeyboardHeight(0)
            }
            Log.d("### Keyboard State", "keyboardHeight : $keyboardHeight keyboardVisibility: $imeVisible")
            insets
        }
    }
}


@Composable
fun keyboardAsState(): MutableState<Int> {
    val keyboardState = remember { mutableStateOf(0) }
    val view = LocalView.current
    LaunchedEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val keyboardHeight = imeHeight - navigationBarHeight
            if (imeVisible && keyboardHeight > 0) {
                keyboardState.value = (keyboardHeight)
            } else {
                keyboardState.value = 0
            }
            Log.d("### keyboardAsState()", "keyboardHeight : $keyboardHeight keyboardVisibility: $imeVisible")
            insets
        }
    }
    return keyboardState
}



@Composable
fun keyboardAsState2(): State<Int> {
    val keyboardState = remember { mutableStateOf(0) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            Log.d("### keyboardAsState2()", "keyboardHeight : $keypadHeight ")
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                keypadHeight
            } else {
                0
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}