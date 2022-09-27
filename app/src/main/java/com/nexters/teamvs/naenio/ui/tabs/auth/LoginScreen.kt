package com.nexters.teamvs.naenio.ui.tabs.auth

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.extensions.requireActivity
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.theme.Font

//TODO 의식의 흐름으로 개발해서 리팩토링 필요

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
    onNickName: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        viewModel.navigationEvent.collect {
            when (it) {
                LoginDestination.Main -> {
                    onNext.invoke()
                }
                LoginDestination.ProfileSettings -> {
                    onNickName.invoke()
                }
            }
        }
    })

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

    LoginScreenContent(
        navController = navController,
        onGoogleLogin = {
            startForResult.launch(viewModel.getGoogleLoginAuth(context.requireActivity()).signInIntent)
        },
        onKakaoLogin = {
            viewModel.loginKakao(context)
        }
    )

//    Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan), onClick = {
//        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
//            link = Uri.parse("https://naenioapp.page.link/")
//            domainUriPrefix = "https://naenioapp.page.link"
//            androidParameters("com.nexters.teamversus.naenio") {
//                fallbackUrl = Uri.parse("https://naver.com")
//            }
//        }
//        Log.d("###", "dynamicLink 생성 테스트:: ${dynamicLink.uri.toString()}")
//    }) {
//        Text(text = "다이나믹 링크 생성 테스트")
//    }
}

@Composable
fun LoginScreenContent(
    navController: NavHostController,
    onGoogleLogin: () -> Unit,
    onKakaoLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xffcc69ff),
                        Color(0xff5661ff)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xB3000000),
                            Color(0xFF000000)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(106.dp)
                        .height(95.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = ""
                )
                Image(
                    modifier = Modifier.padding(top = 17.dp),
                    painter = painterResource(id = R.drawable.ic_wordmark),
                    contentDescription = ""
                )
            }

            Column(
                modifier = Modifier
            ) {
                GoogleLoginButton(onGoogleLogin = onGoogleLogin)
                Spacer(Modifier.height(7.dp))
                KakaoLoginButton(onKakaoLogin = onKakaoLogin)
            }

            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 21.dp)
                    .fillMaxWidth(),
                style = Font.montserratMedium12,
                color = Color(0xff6d6d6d),
                text = "가입 시, 네니오의 다음 사항에 동의하는 것으로 간주합니다."
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 21.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.clickable {
                        // ERROR
                        navController.navigate("loginDetail/${LoginDetailType.SERVICE}")
                    },
                    text = "서비스 이용약관",
                    color = Color.White,
                    style = Font.montserratMedium12
                )
                Text(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    text = "및",
                    color = Color(0xff6d6d6d),
                    style = Font.montserratMedium12
                )
                Text(
                    modifier = Modifier.clickable {
                        // ERROR
                        navController.navigate("loginDetail/${LoginDetailType.PRIVACY}")
                    },
                    text = "개인 정보 정책",
                    color = Color.White,
                    style = Font.montserratMedium12
                )

            }
        }

    }
}

@Composable
fun GoogleLoginButton(
    onGoogleLogin: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(6.dp)
            )
            .height(45.dp)
            .clickable { onGoogleLogin.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(start = 16.dp),
            painter = painterResource(id = R.drawable.login_google),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.google_login),
            style = Font.pretendardMedium16,
            color = Color.Black
        )
    }
}

@Composable
fun KakaoLoginButton(
    onKakaoLogin: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .background(
                color = Color.Yellow,
                shape = RoundedCornerShape(6.dp)
            )
            .height(45.dp)
            .clickable {
                onKakaoLogin.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.padding(start = 16.dp),
            painter = painterResource(id = R.drawable.login_kakao),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .padding(end = 20.dp)
                .weight(1f),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.kakao_login),
            style = Font.pretendardMedium16,
            color = Color.Black
        )
    }
}

object LoginDetailType {
    const val PRIVACY = "privacy"
    const val SERVICE = "service"
}

@Preview
@Composable
fun LoginScreenPreview() {

}