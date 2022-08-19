package com.nexters.teamvs.naenio.ui.feed

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.graphs.Route
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.comment.CommentEvent
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.home.ThemeItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    type: String = "feed",
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))
//    viewModel.setType(type) // TODO 수정

    val posts = viewModel.posts.collectAsState()

    val themeItem = viewModel.themeItem.collectAsState()

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (type == "feed") {
            FeedScreenContent(
                navController = navController,
                posts = posts,
                openSheet = openSheet,
                composition = composition,
                viewModel = viewModel
            )
        } else {
            ThemeDetailLayout(
                themeItem = themeItem.value,
                navController = navController,
                posts = posts,
                openSheet = openSheet,
                composition = composition
            )
        }
    }
}

@Composable
fun ThemeDetailLayout(
    themeItem: ThemeItem,
    navController: NavHostController,
    posts: State<List<Post>>,
    openSheet: (BottomSheetType) -> Unit,
    composition: LottieComposition?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(themeItem.backgroundColorList)
            )
    ) {
        Text(
            modifier = Modifier.padding(top = 19.dp, start = 20.dp),
            text = themeItem.title,
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
}

@Composable
fun FeedScreenContent(
    navController: NavHostController,
    posts: State<List<Post>>,
    openSheet: (BottomSheetType) -> Unit,
    composition: LottieComposition?,
    viewModel: FeedViewModel
) {
    /**
     * FeedScreenContent 에서만 필요한 State 이기 때문에 해당 컴포저블 내에서 상태를 갖도록 함.
     * 테마에서는 이 상태를 알 필요가 없음.
     */
    val feedTabItems = viewModel.feedTabItems.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            FeedTopBar(text = stringResource(id = R.string.bottom_item_feed))

            FeedTabRow(
                feedTabItems = feedTabItems.value,
                selectedTab = selectedTab.value,
                onSelectTab = {
                    viewModel.selectTab(it)
                }
            )
            if (posts.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(id = R.drawable.icon_empty),
                        contentDescription = "icon_empty"
                    )
                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = stringResource(id = R.string.feed_empty),
                        style = Font.pretendardMedium18,
                        color = MyColors.darkGrey_828282
                    )
                }
            } else {
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

@Composable
fun FeedTopBar(text: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 10.dp),
        text = text,
        style = Font.montserratSemiBold24,
        color = Color.White
    )
}

@Composable
fun FeedTabRow(
    feedTabItems: List<FeedTabItemModel>,
    selectedTab: FeedTabItemModel,
    onSelectTab: (FeedTabItemModel) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(feedTabItems) {
            FeedTabItem(
                feedTabItemModel = it,
                selectedTab = selectedTab,
                onClick = onSelectTab
            )
        }
    }
}

@Composable
fun FeedTabItem(
    feedTabItemModel: FeedTabItemModel,
    selectedTab: FeedTabItemModel,
    onClick: (FeedTabItemModel) -> Unit,
) {
    val isSelected = feedTabItemModel == selectedTab
    Row(
        modifier = Modifier
            .background(
                color = if (isSelected) MyColors.pink else MyColors.blue_3979F2,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .noRippleClickable {
                onClick.invoke(feedTabItemModel)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (feedTabItemModel.image != null) {
            Image(painter = painterResource(id = feedTabItemModel.image), contentDescription = "")
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = feedTabItemModel.title,
            style = Font.pretendardSemiBold14,
            color = Color.White
        )
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
            Modifier.padding(top = 20.dp)
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
            Log.d("####", "Feed Item Click")
            navController.navigate("FeedDetail/feedDetail=${page}")
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            ProfileNickName(
                nickName = post.author?.nickname.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 24.dp),
                isIconVisible = true
            )
            VoteContent(post, Modifier, 2)
            VoteGageBar(gage, true)
        }
        Spacer(modifier = Modifier.weight(1f))
        CommentLayout(
            post.commentCount,
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

object FeedType {
    const val ALL_VOTE = "all_vote"
    const val MY_POSTED_VOTE = "my_posted_vote"
    const val MY_VOTE = "my_vote"
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FeedScreenPreview() {
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