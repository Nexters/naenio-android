package com.nexters.teamvs.naenio.utils

import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.flow.MutableStateFlow

class KeyboardUtils {

    companion object {
        val keyboardHeight = MutableStateFlow(0)
    }

    private fun setKeyboardHeight(heightPx: Int) {
        keyboardHeight.value = heightPx
    }

    fun setKeyboardListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val keyboardHeight = imeHeight - navigationBarHeight
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