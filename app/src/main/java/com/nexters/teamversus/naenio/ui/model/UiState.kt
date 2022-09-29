package com.nexters.teamversus.naenio.ui.model

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val exception: Exception) : UiState()
}