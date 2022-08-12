package com.nexters.teamvs.naenio.ui.model

import androidx.annotation.StringRes
import com.nexters.teamvs.naenio.R

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val error: Error) : UiState()
}

sealed class Error(@StringRes val message: Int) {
    object Network : Error(R.string.net_conn_err_message)
    object Unknown : Error(R.string.unknown_err_body)
}