package com.nexters.teamvs.naenio.ui.tabs

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.feed.composables.ProfileImageIcon

@Composable
fun ProfileScreen(navController: NavHostController, modifier: Modifier) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .padding()
        .background(MyColors.darkGrey_313643, shape = RoundedCornerShape(10.dp))
        .padding(horizontal = 24.dp, vertical = 22.dp)
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
                ProfileImageIcon(size = 62.dp)
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "UserName",
                    style = Font.pretendardSemiBold22,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding()
                        .background(MyColors.darkGrey_313643, shape = RoundedCornerShape(5.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = Font.montserratSemiBold14,
                    color = Color.White,
                    text = stringResource(id = com.nexters.teamvs.naenio.R.string.edit))
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = 30.dp))
            ProfileButton(
                modifier = buttonModifier,
                title = stringResource(id = R.string.profile_social_login),
                image = painterResource(id = R.drawable.icon_social_login),
                isLoginLayout = true,
                loginType = "KAKAO" // TODO : LoginType 수정 필요
            )
        }
        item {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            ProfileButton(
                modifier = buttonModifier,
                title = stringResource(id = R.string.profile_my_comment),
                image = painterResource(id = R.drawable.icon_pencil)
            )
        }



    }
}

@Composable
fun ProfileButton(modifier: Modifier,
                  title : String,
                  image: Painter,
                  isLoginLayout : Boolean = false,
                  loginType : String = ""
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = title
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            style = Font.pretendardSemiBold16,
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isLoginLayout) {
            val titleStringId = if(loginType == "KAKAO") R.string.kakao else R.string.google
            val imagePainterId = if(loginType == "KAKAO") R.drawable.login_kakao else R.drawable.login_google
            Row() {
                Image(
                    painter = painterResource(id = imagePainterId),
                    contentDescription = "login_img"
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
    }
}

@Composable
@Preview
fun Profile() {
    ProfileScreen(navController = NavHostController(LocalContext.current), modifier = Modifier)
}