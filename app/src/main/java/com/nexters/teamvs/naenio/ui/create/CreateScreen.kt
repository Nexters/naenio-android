package com.nexters.teamvs.naenio.ui.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun CreateScreen() {
    Box(
        modifier = Modifier
            .background(MyColors.screenBackgroundColor)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Column {
            CreateTopBar()
            Spacer(modifier = Modifier.height(28.dp))

            VoteTopicInput()
            Spacer(modifier = Modifier.height(20.dp))

            VoteOptionsInput()
            Spacer(modifier = Modifier.height(20.dp))

            VoteContentInput()
        }
    }
}

@Composable
fun CreateTopBar() {
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
    hint: String,
    maxLength: Int
) {
    var text by remember { mutableStateOf("") }
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
                text = it
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
fun VoteTopicInput() {
    CreateTitle(
        title = stringResource(id = R.string.vote_topic),
        require = true
    )
    CreateTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 108.dp),
        maxLength = 70,
        hint = stringResource(id = R.string.vote_topic_hint)
    )
}

@Composable
fun VoteOptionsInput() {
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
                hint = stringResource(id = R.string.vote_options_a_hint)
            )
            Spacer(modifier = Modifier.height(18.dp))
            CreateTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 70.dp),
                maxLength = 32,
                hint = stringResource(id = R.string.vote_options_b_hint)
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
fun VoteContentInput() {
    CreateTitle(
        title = stringResource(id = R.string.vote_content),
        require = false
    )
    CreateTextField(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 108.dp),
        maxLength = 99,
        hint = stringResource(id = R.string.vote_content_hint)
    )
}