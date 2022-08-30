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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.feed.composables.TopBar
import com.nexters.teamvs.naenio.ui.profile.ProfileType

@Composable
fun LoginDetailScreen(
    type: String,
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit,
        block = {
            viewModel.setLoginDetailType(type)
        })

    var title = ""
    when (type) {
        LoginDetailType.PRIVACY -> title = "개인정보 정책"
        LoginDetailType.SERVICE -> title = "서비스 이용약관"
    }

    val contentState = viewModel.loginDetailText.collectAsState()
    val content = contentState.value
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
    ) {
        item {
            TopBar(
                modifier = Modifier,
                barTitle = title,
                navController = navController,
                isMoreBtnVisible = false
            )
        }
        item {
            Text(
                text = content.orEmpty(),
                color = Color(0xff6d6d6d),
                style = Font.pretendardRegular14
            )
        }
    }
}