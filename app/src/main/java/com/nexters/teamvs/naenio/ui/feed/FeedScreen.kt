package com.nexters.teamvs.naenio.ui.feed

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.comment.CommentEvent
import com.nexters.teamvs.naenio.ui.model.Comment
import com.nexters.teamvs.naenio.ui.tabs.bottomBarHeight
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modifier: Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
    ) {
        Text(
            modifier = Modifier.padding(top = 19.dp, start = 20.dp),
            text = stringResource(id = R.string.bottom_item_feed),
            fontSize = 24.sp,
            color = Color.White
        )
        FeedPager(modifier, openSheet)
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedPager(
    modifier: Modifier,
    openSheet: (BottomSheetType) -> Unit,
) {
    VerticalPager(
        count = 10,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ) { page ->
        Box(
            Modifier
                .padding(top = 20.dp)
        ) {
            FeedItem(
                page = page,
                openSheet = openSheet
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedItem(page: Int, openSheet: (BottomSheetType) -> Unit) {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Feed Screen $page",
            color = Color.White,
            fontSize = 17.sp
        )
        Button(
            onClick = {
                openSheet(
                    BottomSheetType.CommentType(
                        comments = Comment.mock,
                        onEvent = {
                            Log.d("### FeedScreen", "$it")
                            when(it) {
                                is CommentEvent.Like -> {
                                }
                                CommentEvent.More -> {
                                }
                                is CommentEvent.Write -> {
                                }
                                CommentEvent.Close -> {
                                }
                            }
                        }
                    )
                )
            }
        ) {
            Text(text = "Open bottom sheet Comment")
        }
        Button(onClick = { openSheet(BottomSheetType.Menu) }) {
            Text(text = "Open bottom sheet Menu")
        }
        GageBar(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .height(59.dp),
            gageModifier = Modifier.height(59.dp),
            width = 300.dp,
            backgroundColor = Color.Gray,
            foregroundColor = Brush.horizontalGradient(
                listOf(
                    Color(0xffFD7D20),
                    Color(0xffFBE41A)
                )
            ),
            percent = gage,
        )
    }
}

@Composable
fun GageBar(
    modifier: Modifier,
    gageModifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Float,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(width)
    ) {
        Box(
            modifier = gageModifier
                .background(foregroundColor)
                .width(width * percent)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FeedScreenPreview() {
    FeedScreen(
        navController = NavHostController(LocalContext.current),
        modalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden),
        modifier = Modifier.padding(bottomBarHeight),
        openSheet = {},
        closeSheet = {}
    )
}