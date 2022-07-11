package com.nexters.teamversus.naenio.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.nexters.teamversus.naenio.extensions.fragmentComposeView
import com.nexters.teamversus.naenio.utils.loginWithKakao
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return fragmentComposeView.apply {
            setContent {
                HomeScreen {
                    loginKakao()
                }
            }
        }
    }

    private fun loginKakao() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val token = loginWithKakao(requireActivity())
                Log.d("### kakao token", "$token")
            } catch (e: Exception) {
                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                    Log.d("MainActivity", "사용자가 명시적으로 취소")
                } else {
                    Log.e("MainActivity", "인증 에러 발생", e)
                }
            }
        }
    }

}

@Composable
fun HomeScreen(
    onClick: () -> Unit
) {
    Column(modifier = Modifier.background(Color.LightGray)) {
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow), onClick = {
            onClick.invoke()
        }) {

        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen {

    }
}