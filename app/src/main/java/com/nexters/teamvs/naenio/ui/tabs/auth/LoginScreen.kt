package com.nexters.teamvs.naenio.ui.tabs.auth

import android.app.Activity
import android.net.Uri
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
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.extensions.getActivity

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
                text = "?????????"
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
                text = "??????"
            )
        }
        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Magenta), onClick = {
            onNickName.invoke()
        }) {
            Text(
                text = "????????? ?????? ????????????"
            )
        }

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue), onClick = {
            onNext.invoke()
        }) {
            Text(
                text = "?????? ????????????"
            )
        }

        Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan), onClick = {
            val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                        link = Uri.parse("https://naenioapp.page.link/")
                        domainUriPrefix = "https://naenioapp.page.link"
                        androidParameters("com.nexters.teamversus.naenio") {
                            fallbackUrl = Uri.parse("https://naver.com")
                        }
            }
            Log.d("###", "dynamicLink ?????? ?????????:: ${dynamicLink.uri.toString()}")
        }) {
            Text(text = "???????????? ?????? ?????? ?????????")
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(viewModel = viewModel(), {}) {

    }
}