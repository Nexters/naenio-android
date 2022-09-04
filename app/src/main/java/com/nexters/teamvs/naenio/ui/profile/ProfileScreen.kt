package com.nexters.teamvs.naenio.ui.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.NaenioApp
import com.nexters.teamvs.naenio.base.UiEvent
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.component.DialogModel
import com.nexters.teamvs.naenio.ui.feed.composables.ProfileImageIcon

@Composable
fun ProfileScreen(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val myProfile = viewModel.myProfile.collectAsState()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background(MyColors.screenBackgroundColor)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                myProfile.value?.profileImageIndex?.let {
                    ProfileImageIcon(size = 62.dp, index = it)
                }
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = myProfile.value?.nickname.orEmpty(),
                    style = Font.pretendardSemiBold22,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding()
                        .background(MyColors.darkGrey_313643, shape = RoundedCornerShape(5.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            navController.navigate(AuthScreen.ProfileSetting.route)
                        },
                    style = Font.montserratSemiBold14,
                    color = Color.White,
                    text = stringResource(id = R.string.edit)
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 30.dp))
            ProfileButton(
                profileViewModel = viewModel,
                navController = navController,
                modifier = Modifier.background(
                    color = MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp)
                ),
                title = stringResource(id = R.string.profile_social_login),
                image = painterResource(id = R.drawable.icon_social_login),
                isLoginLayout = true,
                loginType = myProfile.value?.authServiceType.orEmpty() // TODO : LoginType 수정 필요
            )
        }
        item {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            ProfileButton(
                profileViewModel = viewModel,
                navController = navController,
                modifier = Modifier
                    .padding()
                    .background(
                        color = MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp)
                    ),
                title = stringResource(id = R.string.profile_my_comment),
                image = painterResource(id = R.drawable.icon_pencil),
                clickType = ProfileType.MY_COMMENT
            )
        }
        item {
            Spacer(
                modifier = Modifier.padding(top = 20.dp)
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(
                        color = MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp)
                    )
            ) {
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.profile_notice),
                    image = painterResource(id = R.drawable.icon_speaker),
                    clickType = ProfileType.NOTICE
                )
                ProfileButtonLine()
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.profile_question),
                    image = painterResource(id = R.drawable.icon_question),
                    clickType = ProfileType.QUESTION
                )
                ProfileButtonLine()
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.profile_developer),
                    image = painterResource(id = R.drawable.icon_person),
                    clickType = ProfileType.DEVELOPER
                )
                ProfileButtonLine()
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.profile_version),
                    image = painterResource(id = R.drawable.icon_phone),
                    clickType = ProfileType.VERSION

                )
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .wrapContentHeight()
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp))
            ) {
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.logout),
                    image = painterResource(id = R.drawable.icon_logout),
                    clickType = ProfileType.LOGOUT

                )
                ProfileButtonLine()
                ProfileButton(
                    profileViewModel = viewModel,
                    navController = navController,
                    title = stringResource(id = R.string.profile_signout),
                    image = painterResource(id = R.drawable.icon_signout),
                    clickType = ProfileType.SIGNOUT
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 28.dp))
        }
    }
}

@Composable
fun ProfileButtonLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 9.dp)
            .height(1.dp)
            .background(MyColors.darkGrey_424A5C)
    )
}

@Composable
fun ProfileButton(
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    title: String,
    image: Painter,
    isLoginLayout: Boolean = false,
    loginType: String = "",
    clickType: String = ""
) {
    Row(
        modifier = modifier
            .height(60.dp)
            .clickable {
                if (clickType != "") {
                    when (clickType) {
                        ProfileType.MY_COMMENT -> moveProfileDetailScreen(
                            navController, ProfileType.MY_COMMENT
                        )
                        ProfileType.NOTICE -> moveProfileDetailScreen(
                            navController, ProfileType.NOTICE
                        )
                        ProfileType.QUESTION -> setQuestionBtn()
                        ProfileType.DEVELOPER -> moveProfileDetailScreen(
                            navController, ProfileType.DEVELOPER
                        )
                        ProfileType.VERSION -> moveProfileDetailScreen(
                            navController, ProfileType.VERSION
                        )
                        ProfileType.LOGOUT -> setLogoutBtn(
                            viewModel = profileViewModel,
                            navController = navController
                        )
                        ProfileType.SIGNOUT -> {
                            setSignOutBtn(
                                viewModel = profileViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        Image(
            painter = image, contentDescription = title
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            style = Font.pretendardSemiBold16,
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isLoginLayout) {
            val titleStringId = if (loginType == "KAKAO") R.string.kakao else R.string.google
            val imagePainterId =
                if (loginType == "KAKAO") R.drawable.login_kakao else R.drawable.login_google
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imagePainterId), contentDescription = "login_img"
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = titleStringId),
                    style = Font.pretendardMedium16,
                    color = Color.White
                )
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.icon_back_s),
                contentDescription = "icon_back_s"
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
    }
}


fun moveProfileDetailScreen(navController: NavHostController, type: String) {
    navController.navigate("profileDetail/${type}")
}

private fun setQuestionBtn() {
    Log.d("### ProfileScreen", ProfileType.QUESTION)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(NaenioApp.context.getString(R.string.question_url)))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    NaenioApp.context.startActivity(intent)
}

private fun setLogoutBtn(
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    Log.d("### ProfileScreen", ProfileType.LOGOUT)
    GlobalUiEvent.uiEvent.tryEmit(
        UiEvent.ShowDialog(
            DialogModel(title = "로그아웃",
                message = "로그아웃 하시겠습니까?",
                button1Text = "닫기",
                button2Text = "로그아웃",
                button1Callback = {
                    Log.d("####", "LogoutDialog - Exit")
                    GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
                },
                button2Callback = {
                    Log.d("####", "LogoutDialog - Logout")
                    viewModel.logout()
                    GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
                    navController.navigate(AuthScreen.Login.route)
                })
        )
    )
}


private fun setSignOutBtn(
    navController: NavHostController,
    viewModel: ProfileViewModel,
) {
    Log.d("### ProfileScreen", ProfileType.SIGNOUT)
    GlobalUiEvent.uiEvent.tryEmit(
        UiEvent.ShowDialog(
            DialogModel(title = "회원탈퇴",
                message = "정말 탈퇴 하시겠어요?",
                button1Text = "닫기",
                button2Text = "탈퇴하기",
                button1Callback = {
                    Log.d("####", "SignoutDialog - Exit")
                    GlobalUiEvent.uiEvent.tryEmit(UiEvent.HideDialog)
                },
                button2Callback = {
                    Log.d("####", "SignoutDialog - Signout")
                    viewModel.signOut()
                    navController.navigate(AuthScreen.Login.route)
                })
        )
    )
}

object ProfileType {
    const val MY_COMMENT = "comment"
    const val NOTICE = "notice"
    const val QUESTION = "question"
    const val DEVELOPER = "developer"
    const val VERSION = "version"
    const val LOGOUT = "logout"
    const val SIGNOUT = "signout"
    const val NOTICE_DETAIL = "notice_detail"
}

@Composable
@Preview
fun Profile() {
    ProfileScreen(navController = NavHostController(LocalContext.current), modifier = Modifier)
}