package com.nexters.teamvs.naenio.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.BuildConfig
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.feed.ProfileImageIcon
import com.nexters.teamvs.naenio.ui.feed.TopBar

@Composable
fun ProfileDetailScreen(profileType : String = "", navController: NavHostController) {
    var title = ""
    var isMoreBtnVisible = false
    when(profileType) {
        ProfileType.MY_COMMENT -> {
            title = "작성한 댓글"
            isMoreBtnVisible = true
        }
        ProfileType.DEVELOPER -> {
            title = "개발자 정보"
            isMoreBtnVisible = false
        }
        ProfileType.VERSION -> {
            title = "버전 정보"
            isMoreBtnVisible = false
        }
        ProfileType.NOTICE -> {
            title = "공지사항"
            isMoreBtnVisible = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
    ) {
        TopBar(
            modifier = Modifier,
            barTitle = title,
            navController = navController,
            isMoreBtnVisible = isMoreBtnVisible)
        when(profileType) {
            ProfileType.MY_COMMENT -> {

            }
            ProfileType.DEVELOPER -> {
                DeveloperLayout()
            }
            ProfileType.VERSION -> {
                VersionLayout()
            }
            ProfileType.NOTICE -> {
            }
        }
    }
}

@Composable
fun MyCommentLayout() {

}

@Composable
fun VersionLayout() {
    Column(
        modifier = Modifier.fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "app_icon"
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = "현재 버전",
            color = Color.White,
            style = Font.pretendardSemiBold16
        )
        Text(
            text = BuildConfig.VERSION_NAME,
            color = MyColors.grey_b3b3b3,
            style = Font.pretendardSemiBold16,
            lineHeight = 32.sp
        )
    }
}

@Composable
fun DeveloperLayout() {

}


@Composable
@Preview
fun ProfileDetail() {
    ProfileDetailScreen(navController = NavHostController(LocalContext.current), profileType = "version")
}