package com.nexters.teamversus.naenio.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.kakao.sdk.user.UserApiClient
import com.nexters.teamversus.naenio.extensions.fragmentComposeView

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
        UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, error ->
            if (error != null) {
                Log.e("###", "로그인 실패", error)
            } else if (token != null) {
                Log.i("###", "로그인 성공 ${token.accessToken}")
            }
        }
    }

}

@Composable
fun HomeScreen(
    onClick: () -> Unit
) {
    Column(modifier = Modifier.background(Color.LightGray)) {
        Button(onClick = {
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