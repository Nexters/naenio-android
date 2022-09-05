package com.nexters.teamvs.naenio.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent
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


data class ErrorResponse(
    val message: String,
    val code: String,
)

fun HttpException.getErrorMessage(): String {
    val errorString = this.response()?.errorBody()?.string().also {
        Log.d("### errorString", "$it")
    }
    val errorDto: ErrorResponse? = Gson().fromJson<ErrorResponse>(
        errorString, ErrorResponse::class.java
    )
    val errorMessage = errorDto?.message
    val errorCode = errorDto?.code

    //TODO 서버에서 401을 내려주기로 한 것 아닌가?
    if (errorCode == "FAIL" && errorMessage?.contains("Authorization") == true) {
        GlobalUiEvent.forceLogout()
    }
    return if (errorMessage.isNullOrEmpty()) {
        "일시적 오류가 발생했습니다. 잠시 후 재시도 해주세요."
    } else errorMessage
}

fun Exception.errorMessage(): String {
    printStackTrace()
    return if (isNetworkException()) {
        if (this is HttpException) {
            this.getErrorMessage()
        } else "네트워크 연결 상태를 확인해주세요."
    } else {
        "일시적 오류가 발생했습니다. 잠시 후 재시도 해주세요."
    }
}


inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
