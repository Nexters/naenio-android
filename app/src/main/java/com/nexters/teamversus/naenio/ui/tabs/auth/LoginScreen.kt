package com.nexters.teamversus.naenio.ui.tabs.auth

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.nexters.teamversus.naenio.extensions.getActivity

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNickName: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    viewModel.handleGoogleSignInResult(task)
                }
            } else {
                Log.d("###", "${result.resultCode}")
            }
        }

    Column(modifier = Modifier.background(Color.LightGray)) {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
            onClick = { viewModel.loginKakao(context) }
        ) {
            Text(
                text = "카카오"
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            onClick = {
                startForResult.launch(
                    viewModel.getGoogleLoginAuth(
                        context.getActivity() ?: return@Button
                    ).signInIntent
                )
            }
        ) {
            Text(
                text = "구글"
            )
        }
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Magenta), onClick = {
            onNickName.invoke()
        }) {
            Text(
                text = "닉네임 설정 화면으로"
            )
        }

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue), onClick = {
            onNext.invoke()
        }) {
            Text(
                text = "메인 화면으로"
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(viewModel = viewModel(), {}) {

    }
}