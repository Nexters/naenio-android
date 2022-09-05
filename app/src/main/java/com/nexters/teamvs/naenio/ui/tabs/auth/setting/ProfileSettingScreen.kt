package com.nexters.teamvs.naenio.ui.tabs.auth.setting

import android.util.Log
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
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.graphs.AuthScreen
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.MyShape
import com.nexters.teamvs.naenio.ui.model.UiState
import com.nexters.teamvs.naenio.ui.tabs.auth.model.Profile
import com.nexters.teamvs.naenio.ui.tabs.auth.model.ProfileImageModel

const val MAX_NICKNAME_LENGTH = 10

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileSettingScreen(
    navController: NavHostController,
    viewModel: ProfileSettingViewModel,
    onClose: () -> Unit,
    onNext: () -> Unit,
) {
    LaunchedEffect(key1 = Unit, block = {
        viewModel.uiState.collect {
            when (it) {
                is UiState.Error -> {
                    val errorMessage = if (it.exception is AlreadyIsExistNickNameException) {
                        "이미 존재하는 닉네임입니다. 다른 닉네임을 사용해주세요 ㅠㅠ."
                    } else {
                        it.exception.errorMessage()
                    }
                    GlobalUiEvent.showToast(errorMessage)
                    GlobalUiEvent.hideLoading()
                }
                UiState.Idle -> {

                }
                UiState.Loading -> {
                    GlobalUiEvent.showLoading()
                }
                UiState.Success -> {
                    GlobalUiEvent.hideLoading()
                    onNext.invoke()
                }
            }
        }
    })

    ProfileSettingScreenContent(
        navController = navController,
        viewModel = viewModel,
        onClose = onClose
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileSettingScreenContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ProfileSettingViewModel,
    onClose: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val user = viewModel.user.collectAsState()
    var inputText by remember { mutableStateOf(user.value?.nickname ?: "") }
    var isVisibleDialog by remember { mutableStateOf<Boolean>(false) }
    var isSavable by remember { mutableStateOf(false) }
    var selectedProfileImage by remember {
        mutableStateOf<Int>(
            user.value?.profileImageIndex ?: (0 until Profile.images.size).random()
        )
    }

    BackHandler {
        if (isVisibleDialog) {
            isVisibleDialog = false
        } else {
            Log.d(
                "###",
                "navController.previousBackStackEntry: ${navController.previousBackStackEntry}"
            )
            navController.popBackStack()
            if (navController.previousBackStackEntry == null) {
                navController.navigate(AuthScreen.Login.route)
            }
        }
    }

    isSavable = inputText.isNotBlank() && inputText.length >= 2

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor),
    ) {
        Column {
            ProfileSettingTopBar(
                isEnabled = isSavable,
                onSave = {
                    viewModel.setProfileInfo(inputText, selectedProfileImage)
                },
                onClose = onClose
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
    onClose: () -> Unit,
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
            painter = painterResource(id = R.drawable.icon_back_m),
            modifier = Modifier.clickable {
                onClose.invoke()
            },
            contentDescription = ""
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
    profileImage: Int,
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
            painter = painterResource(id = Profile.images[profileImage].image),
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
    onSelect: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .noRippleClickable { onClose.invoke() }
    ) {
        Column(
            modifier = Modifier
                .align(BottomCenter)
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
                            onSelect.invoke(model.id)
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
    ProfileSettingScreenContent(
        navController = NavHostController(LocalContext.current),
        viewModel = viewModel(),
        onClose = {}
    )
}