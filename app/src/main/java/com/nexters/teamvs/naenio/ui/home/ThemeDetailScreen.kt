package com.nexters.teamvs.naenio.ui.home

import com.nexters.teamvs.naenio.ui.feed.*
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.Route
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.comment.CommentEvent
import com.nexters.teamvs.naenio.ui.composables.Toast
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.model.UiState
import com.nexters.teamvs.naenio.ui.tabs.bottomBarHeight
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeDetailScreen(
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modifier: Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))
    val posts = viewModel.posts.collectAsState()

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val scope = rememberCoroutineScope()

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.uiState.collect {
            Log.d("### FeedScreen", "$it")
            when (it) {
                is UiState.Error -> {
                    snackbarHostState.showSnackbar(it.exception.errorMessage())
                }
                UiState.Idle -> {

                }
                UiState.Loading -> {

                }
                UiState.Success -> {
                    snackbarHostState.showSnackbar("Success")
                }
            }
        }
    })

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Toast(message = it.message)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MyColors.screenBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(top = 19.dp, start = 20.dp),
                    text = stringResource(id = R.string.bottom_item_feed),
                    fontSize = 24.sp,
                    color = Color.White
                )
                Box {
                    FeedPager(
                        modifier = Modifier,
                        posts = posts.value,
                        openSheet = openSheet,
                        navController = navController
                    )
                    LottieAnimation(
                        composition,
                        modifier = Modifier.wrapContentSize(),
                        iterations = Int.MAX_VALUE
                    )
                }
            }
            IconButton(
                onClick = {
                    navController.navigate(Route.Create)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_floating),
                    tint = Color.Unspecified,
                    contentDescription = "floating"
                )
            }
        }
    }
}

@Composable
@Preview
fun FeedScreenBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_floating),
                contentDescription = "floating"
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedPager(
    modifier: Modifier,
    posts: List<Post>,
    openSheet: (BottomSheetType) -> Unit,
    navController: NavHostController
) {
    VerticalPager(
        count = posts.size,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize(),
    ) { page ->
        Box(
            Modifier
                .padding(top = 20.dp)
        ) {
            FeedItem(
                page = page,
                post = posts[page],
                openSheet = openSheet,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedItem(
    page: Int,
    post: Post,
    openSheet: (BottomSheetType) -> Unit,
    navController: NavHostController
) {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            MyColors.postBackgroundColor,
            shape = RoundedCornerShape(16.dp)
        )
        .clickable {
            Log.d("###", "clcik?")
            navController.navigate(Graph.DETAILS)
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            ProfileNickName(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 24.dp), true
            )
            VoteContent(Modifier, 2)
            VoteGageBar(gage, true)
        }
        Spacer(modifier = Modifier.weight(1f))
        CommentLayout(
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    MyColors.darkGrey_1e222c,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(horizontal = 20.dp)
                .clickable {
                    openSheet(
                        BottomSheetType.CommentType(
                            postId = post.id,
                            onEvent = {
                                Log.d("### FeedScreen", "$it")
                                when (it) {
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
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun HomeDetailScreen() {
//    FeedScreen(
//        navController = NavHostController(LocalContext.current),
//        modalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden),
//        modifier = Modifier.padding(bottomBarHeight),
//        openSheet = {},
//        closeSheet = {},
//        viewModel = viewModel()
//    )
//    FeedPager(Modifier) {
//
//    }
}