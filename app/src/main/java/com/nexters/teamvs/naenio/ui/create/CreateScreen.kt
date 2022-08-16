package com.nexters.teamvs.naenio.ui.create

import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.extensions.requireActivity
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.theme.Font.montserratBold18
import com.nexters.teamvs.naenio.theme.Font.montserratMedium12
import com.nexters.teamvs.naenio.theme.Font.pretendardMedium16
import com.nexters.teamvs.naenio.theme.Font.pretendardSemiBold18
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.base.UiEvent

/**
 * uiState 별 대응
 * 완료 시 피드에 추가
 */
@Composable
fun CreateScreen(
    navController: NavHostController,
    viewModel: CreateViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current
) {

    context.requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("### CreateScreen", " DisposableEffect Event: $event")
            if (event == Lifecycle.Event.ON_STOP) {
                context.requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var title by remember { mutableStateOf("") }
    var voteOption1 by remember { mutableStateOf("") }
    var voteOption2 by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val enabled = title.isNotEmpty() && voteOption1.isNotEmpty() && voteOption2.isNotEmpty()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.createEvent.collect {
            Log.d("### CreateEvent", "$it")
            when (it) {
                is CreateEvent.Error -> {
                    GlobalUiEvent.uiEvent.emit(UiEvent.HideLoading)
                }
                CreateEvent.Loading -> {
                    GlobalUiEvent.uiEvent.emit(UiEvent.ShowLoading)
                }
                is CreateEvent.Success -> {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN) //TODO 피드에 게시한 포스트 추가(?)
                }
            }
        }
    })

    Box(
        modifier = Modifier
            .background(MyColors.screenBackgroundColor)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        LazyColumn {
            item {
                CreateTopBar(enabled = enabled, upload = {
                    if (!enabled) {
                        Log.d("###", "필수 항목들을 모두 입력해주세요!") //TODO Toast
                        return@CreateTopBar
                    }
                    viewModel.createPost(
                        title = title,
                        choices = arrayOf(voteOption1, voteOption2),
                        content = content,
                    )
                })
                Spacer(modifier = Modifier.height(28.dp))
            }

            item {
                VoteTopicInput(title) {
                    title = it
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                VoteOptionsInput(
                    voteOption1 = voteOption1,
                    voteOption2 = voteOption2,
                    onValueChange1 = {
                        voteOption1 = it
                    },
                    onValueChange2 = {
                        voteOption2 = it
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                VoteContentInput(content) {
                    content = it
                }
            }
        }
    }
}

@Composable
fun CreateTopBar(
    enabled: Boolean,
    upload: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "close"
        )

        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.new_post),
            style = montserratBold18,
            color = Color.White
        )

        Text(
            modifier = Modifier.clickable {
                upload.invoke()
            },
            text = stringResource(id = R.string.upload),
            style = pretendardSemiBold18,
            color = if (enabled) MyColors.pink else MyColors.grey4d4d4d
        )
    }
}

@Composable
fun CreateTitle(title: String, require: Boolean) {
    Text(
        modifier = Modifier.padding(bottom = 4.dp),
        text = (if (require) "*" else "") + title,
        style = pretendardMedium16,
        color = Color.White
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateTextField(
    modifier: Modifier,
    text: String,
    hint: String,
    maxLength: Int,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var inputLength by remember { mutableStateOf(0) }
    inputLength = text.length

    Box {
        TextField(
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.onFocusChanged {
                Log.d("### onFocusChanged", "${it.hasFocus} ${it.isFocused} ${it.isCaptured}")
            },
            textStyle = pretendardMedium16,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = MyColors.darkGrey_313643,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            placeholder = {
                Text(
                    style = pretendardMedium16,
                    color = MyColors.darkGrey_828282,
                    text = hint
                )
            },
            value = TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            ),
            onValueChange = {
                if (inputLength >= maxLength) return@TextField
                onValueChange.invoke(it.text.replace("\n", ""))
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            text = "$inputLength/$maxLength",
            color = MyColors.darkGrey_828282,
            style = montserratMedium12
        )
    }
}

@Composable
fun VoteTopicInput(
    title: String,
    onValueChange: (String) -> Unit,
) {
    CreateTitle(
        title = stringResource(id = R.string.vote_topic),
        require = true
    )
    CreateTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 108.dp),
        maxLength = 70,
        hint = stringResource(id = R.string.vote_topic_hint),
        text = title,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        onValueChange = onValueChange
    )
}

@Composable
fun VoteOptionsInput(
    voteOption1: String,
    voteOption2: String,
    onValueChange1: (String) -> Unit,
    onValueChange2: (String) -> Unit,
) {
    CreateTitle(
        title = stringResource(id = R.string.vote_options),
        require = true
    )

    Box {
        Column {
            CreateTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 70.dp),
                maxLength = 32,
                text = voteOption1,
                hint = stringResource(id = R.string.vote_options_a_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = onValueChange1
            )
            Spacer(modifier = Modifier.height(18.dp))
            CreateTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 70.dp),
                maxLength = 32,
                text = voteOption2,
                hint = stringResource(id = R.string.vote_options_b_hint),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onValueChange = onValueChange2
            )
        }
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_vs),
            contentDescription = "vs"
        )
    }
}

@Composable
fun VoteContentInput(
    content: String,
    onValueChange: (String) -> Unit,
) {
    CreateTitle(
        title = stringResource(id = R.string.vote_content),
        require = false
    )
    CreateTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 108.dp),
        maxLength = 99,
        text = content,
        hint = stringResource(id = R.string.vote_content_hint),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = onValueChange
    )
}