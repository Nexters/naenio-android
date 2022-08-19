package com.nexters.teamvs.naenio.base

import com.nexters.teamvs.naenio.ui.component.DialogModel
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 싱글톤 사용 시, 결합도를 높이고 테스트마다 독립적인 인스턴스 사용이 어려워지는 단점이 있으나,
 * 싱글 액티비티 구조이고 UI 레이어에서만 사용하는 이벤트이기 때문에 해당 방식을 채택했습니다.
 *
 * 최상위 화면(MainActivity)에서 사용해야 하는 UI 처리를 위해서만 사용해야 합니다.
 */
object GlobalUiEvent {
    val uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)

    suspend fun showToast(message: String) {
        uiEvent.emit(UiEvent.ShowToast(message))
    }

    suspend fun showLoading() {
        uiEvent.emit(UiEvent.ShowLoading)
    }

    suspend fun hideLoading() {
        uiEvent.emit(UiEvent.HideLoading)
    }

    suspend fun showDialog(dialogModel: DialogModel) {
        uiEvent.emit(UiEvent.ShowDialog(dialogModel))
    }

    fun hideDialog() {
        uiEvent.tryEmit(UiEvent.HideDialog)
    }
}

sealed class UiEvent {
    object None : UiEvent()
    object ShowLoading : UiEvent()
    object HideLoading : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
    data class ShowDialog(val dialogModel: DialogModel) : UiEvent()
    object HideDialog : UiEvent()
}