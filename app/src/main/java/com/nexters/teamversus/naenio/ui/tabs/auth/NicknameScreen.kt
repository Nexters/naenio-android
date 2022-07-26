package com.nexters.teamversus.naenio.ui.tabs.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun NicknameScreen(
    viewModel: LoginViewModel,
    onNext: () -> Unit,
) {
    Column {
        var inputText by remember { mutableStateOf("닉네임") }
        TextField(
            value = inputText,
            onValueChange = {
                inputText = it
            }
        )
        Text(
            text = "닉네임 중복 체크",
            modifier = Modifier.clickable {
                viewModel.isExistNickname(inputText)
            }
        )

        Text(
            text = "닉네임 설정",
            modifier = Modifier.clickable {
                viewModel.setNickname(inputText)
            }
        )

        Text(
            text = "메인 화면으로 이동",
            modifier = Modifier.clickable {
                onNext.invoke()
            }
        )
    }
}