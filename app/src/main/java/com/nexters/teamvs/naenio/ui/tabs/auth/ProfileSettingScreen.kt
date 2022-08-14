package com.nexters.teamvs.naenio.ui.tabs.auth

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.MyShape
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile
import com.nexters.teamvs.naenio.ui.tabs.auth.model.ProfileImageModel

const val MAX_NICKNAME_LENGTH = 10

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileSettingScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    onNext: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var inputText by remember { mutableStateOf("") }
    var isVisibleDialog by remember { mutableStateOf<Boolean>(false) }
    var isSavable by remember { mutableStateOf(false) }
    var selectedProfileImage by remember { mutableStateOf(Profile.images[0]) }

    BackHandler {
        if (isVisibleDialog) {
            isVisibleDialog = false
        } else {
            navController.popBackStack()
        }
    }

    isSavable = inputText.isNotBlank() && inputText.length >= 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor),
    ) {
        Column {
            ProfileSettingTopBar(
                isEnabled = isSavable,
                onSave = {
                    viewModel.setProfileInfo(inputText, selectedProfileImage.id)
                }
            )
            Spacer(modifier = Modifier.height(54.dp))

            ProfileImage(
                modifier = Modifier.align(CenterHorizontally),
                profileImage = selectedProfileImage
            ) {
                isVisibleDialog = true
            }

            Spacer(modifier = Modifier.height(40.dp))

            InputNickname(
                modifier = Modifier.align(CenterHorizontally),
                text = inputText
            ) {
                inputText = it
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = isVisibleDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (isVisibleDialog) keyboardController?.hide()
            SelectImageBottomDialog(
                onClose = { isVisibleDialog = false }
            ) {
                selectedProfileImage = it
                isVisibleDialog = false
            }
        }
    }
}

@Composable
fun ProfileSettingTopBar(
    isEnabled: Boolean,
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_back_left),
            contentDescription = "close"
        )

        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = stringResource(id = com.nexters.teamvs.naenio.R.string.set_profile),
            style = Font.montserratBold18,
            color = Color.White
        )

        Text(
            modifier = Modifier.clickable {
                if (!isEnabled) return@clickable
                onSave.invoke()
            },
            text = stringResource(id = com.nexters.teamvs.naenio.R.string.save),
            style = Font.pretendardSemiBold18,
            color = if (isEnabled) MyColors.pink else MyColors.grey_d9d9d9
        )
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier,
    profileImage: ProfileImageModel,
    onEditProfileImage: () -> Unit,
) {
    Box(
        modifier = modifier.clickable {
            onEditProfileImage.invoke()
        },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = profileImage.image),
            contentDescription = ""
        )
        Image(
            modifier = Modifier.align(Alignment.BottomEnd),
            painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_edit_pan),
            contentDescription = ""
        )
    }
}

@Composable
fun InputNickname(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = modifier.padding(horizontal = 31.dp)) {
        TextField(
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(1.dp, Color(0xff8b95a1)),
                    shape = RoundedCornerShape(8.dp)
                ),
            placeholder = {
                Text(
                    style = Font.pretendardMedium16,
                    color = MyColors.darkGrey_828282,
                    text = "2자 이상 닉네임을 입력해주세요."
                )
            },
            value = text,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = MyColors.darkGrey_313643,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                if (it.length <= MAX_NICKNAME_LENGTH) {
                    onValueChange.invoke(it)
                }
            }
        )
        Text(
            style = Font.pretendardMedium16,
            modifier = Modifier.align(Alignment.End),
            color = Color(0xff828282),
            text = "${text.length}/$MAX_NICKNAME_LENGTH"
        )
    }
}

@Composable
fun SelectImageBottomDialog(
    onClose: () -> Unit,
    onSelect: (ProfileImageModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .noRippleClickable { onClose.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Color(0xff313643), shape = MyShape.TopRoundedCornerShape)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 21.dp, bottom = 11.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    text = "이미지 선택",
                    style = Font.pretendardSemiBold18
                )
                Image(
                    modifier = Modifier.clickable {
                        onClose.invoke()
                    },
                    painter = painterResource(id = com.nexters.teamvs.naenio.R.drawable.ic_close),
                    contentDescription = ""
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xff424a5c))
            )
            LazyVerticalGrid(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(27.dp),
                horizontalArrangement = Arrangement.spacedBy(27.dp),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 27.dp, vertical = 27.dp),
                content = {
                    items(Profile.images) {
                        ProfileImageItem(it) { model ->
                            onSelect.invoke(model)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileImageItem(
    profileImageModel: ProfileImageModel,
    onClick: (ProfileImageModel) -> Unit,
) {
    Image(
        modifier = Modifier.clickable {
            onClick.invoke(profileImageModel)
        },
        painter = painterResource(id = profileImageModel.image),
        contentDescription = ""
    )
}

@Preview
@Composable
fun ProfileImagePreview() {
//    ProfileSettingScreen(
//        viewModel = viewModel(),
//    ) {
//
//    }
    Box(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp)
            .clickable {

            }

    ) {
        Box(
            Modifier
                .background(Color.Red)
                .size(60.dp)
        ) {

        }
    }
}