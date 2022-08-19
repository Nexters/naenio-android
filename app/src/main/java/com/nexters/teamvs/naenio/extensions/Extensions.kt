package com.nexters.teamvs.naenio.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import retrofit2.HttpException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Fragment.fragmentComposeView: ComposeView
    get() {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
    }

fun Context.requireActivity(): Activity {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    throw IllegalStateException("Not Found Activity.")
}

fun Throwable.isNetworkException(): Boolean {
    return when (this) {
        is HttpException -> true
        is UnknownHostException -> true
        is NoRouteToHostException -> true
        is SocketTimeoutException -> true
        is ConnectException -> true
        else -> false
    }
}

fun Exception.errorMessage(): String {
    return if (isNetworkException()) {
        "네트워크 연결 상태를 확인해주세요."
    } else {
        "일시적 오류가 발생했습니다. 재시도 해주세요."
    }
}


@Composable
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier {
    return clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
