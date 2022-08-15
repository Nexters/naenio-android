package com.nexters.teamvs.naenio.ui.create

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font.montserratBold18
import com.nexters.teamvs.naenio.theme.Font.montserratMedium12
import com.nexters.teamvs.naenio.theme.Font.pretendardMedium16
import com.nexters.teamvs.naenio.theme.Font.pretendardSemiBold18
import com.nexters.teamvs.naenio.theme.MyColors

/**
 * uiState 별 대응
 * 입력 텍스트 조건 검증
 * 키보드 액션
 * 키보드 UI 대응
 * API 연동
 * 등록버튼 enable 옵션
 */
@Composable
fun CreateScreen(
    viewModel: CreateViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var voteOption1 by remember { mutableStateOf("") }
    var voteOption2 by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.createEvent.collect {
            Log.d("### CreateEvent", "$it")
            when (it) {
                is CreateEvent.Error -> {

                }
                CreateEvent.Loading -> {

                }
                is CreateEvent.Success -> {

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
        Column {
            CreateTopBar(upload = {
                viewModel.createPost(
                    title = title,
                    choices = arrayOf(voteOption1, voteOption2),
                    content = content,
                )
            })
            Spacer(modifier = Modifier.height(28.dp))

            VoteTopicInput(title) {
                title = it
            }
            Spacer(modifier = Modifier.height(20.dp))

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

            VoteContentInput(content) {
                content = it
            }
        }
    }
}

@Composable
fun CreateTopBar(
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
            color = MyColors.pink
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

@Composable
fun CreateTextField(
    modifier: Modifier,
    text: String,
    hint: String,
    maxLength: Int,
    onValueChange: (String) -> Unit,
) {
    var inputLength by remember { mutableStateOf(0) }
    inputLength = text.length

    Box {
        TextField(
            shape = RoundedCornerShape(8.dp),
            modifier = modifier,
            textStyle = pretendardMedium16,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = MyColors.darkGrey_313643,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    style = pretendardMedium16,
                    color = MyColors.darkGrey_828282,
                    text = hint
                )
            },
            value = text,
            onValueChange = {
                if (inputLength >= maxLength) return@TextField
                onValueChange.invoke(it)
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
        onValueChange = onValueChange
    )
}