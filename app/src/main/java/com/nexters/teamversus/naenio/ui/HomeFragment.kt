package com.nexters.teamversus.naenio.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.nexters.teamversus.naenio.extensions.fragmentComposeView
import com.nexters.teamversus.naenio.utils.loginWithKakao
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val getGoogleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleGoogleSignInResult(task)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return fragmentComposeView.apply {
            setContent {
                HomeScreen (
                    onLoginKakao = {
                        loginKakao()
                    },
                    onLoginGoogle = {
                        loginGoogle()
                    }
                )
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

    private fun loginGoogle() {
        Log.d("MainActivity", "[loginGoogle] start")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        getGoogleLoginResult.launch(signInIntent)
    }

    private fun handleGoogleSignInResult(completedTask : Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.result
            Log.d("MainActivity", "[HandleGoogleSignInResult] success data :: " + account.email)

        } catch (e : ApiException) {
            Log.e("MainActivity", "[HandleGoogleSignInResult] failed code :: " + e.statusCode.toString())
        }
    }
}

@Composable
fun HomeScreen(
    onLoginKakao: () -> Unit,
    onLoginGoogle: () -> Unit
) {
    Column(modifier = Modifier.background(Color.LightGray)) {
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow), onClick = {
            onLoginKakao.invoke()
        }) {

        }
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue), onClick = {
            onLoginGoogle.invoke()
        }) {

        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen (
        onLoginKakao = {
        },
        onLoginGoogle = {
        }
    )
}