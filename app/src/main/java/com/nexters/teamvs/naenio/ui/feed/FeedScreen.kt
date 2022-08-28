package com.nexters.teamvs.naenio.ui.feed

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.noRippleClickable
import com.nexters.teamvs.naenio.graphs.Route
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.comment.CommentEvent
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.feed.composables.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        FeedScreenContent(
            navController = navController,
            openSheet = openSheet,
            viewModel = viewModel
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeedScreenContent(
    navController: NavHostController,
    openSheet: (BottomSheetType) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    /**
     * FeedScreenContent 에서만 필요한 State 이기 때문에 해당 컴포저블 내에서 상태를 갖도록 함.
     * 테마에서는 아래 상태들을 알 필요가 없음.
     */
    val feedTabItems = viewModel.feedTabItems.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()
    val posts = viewModel.posts.collectAsState()
    val isEmptyFeed = posts.value != null && posts.value?.isEmpty() == true

    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(key1 = Unit, block = {
        viewModel.event.collect {
            when (it) {
                FeedEvent.ScrollToTop -> {
                    pagerState.scrollToPage(0)
                }
            }
        }
    })

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
            Spacer(modifier = Modifier.height(20.dp))

            if (isEmptyFeed) {
                FeedEmptyLayout()
            } else {
                FeedPager(
                    modifier = Modifier,
                    pagerState = pagerState,
                    posts = posts.value ?: emptyList(),
                    openSheet = openSheet,
                    onVote = { postId, choiceId ->
                        viewModel.vote(postId = postId, choiceId = choiceId)
                    },
                    loadNextPage = viewModel::loadNextPage,
                    navController = navController
                )
            }
        }
        FeedCreateFloatingButton(modifier = Modifier.align(Alignment.BottomEnd)) {
            navController.navigate(Route.Create)
        }
    }
}

@Composable
fun FeedCreateFloatingButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_floating),
            tint = Color.Unspecified,
            contentDescription = "floating"
        )
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
    pagerState: PagerState,
    openSheet: (BottomSheetType) -> Unit,
    onVote: (Int, Int) -> Unit,
    navController: NavHostController,
    loadNextPage: () -> Unit,
) {
    val threshold = 3
    val lastIndex = posts.lastIndex

    VerticalPager(
        state = pagerState,
        count = posts.size,
        itemSpacing = 20.dp,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize(),
    ) { page ->
        if (page + threshold >= lastIndex) {
            SideEffect {
                Log.d("###", "$page $lastIndex")
                loadNextPage()
            }
        }
        Box {
            FeedItem(
                page = page,
                post = posts[page],
                navController = navController,
                onVote = onVote,
                openSheet = openSheet,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedItem(
    page: Int,
    post: Post,
    navController: NavHostController,
    onVote: (Int, Int) -> Unit,
    openSheet: (BottomSheetType) -> Unit,
) {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            MyColors.postBackgroundColor,
            shape = RoundedCornerShape(16.dp)
        )
        .clickable {
            Log.d("####", "Feed Item Click")
            navController.navigate("FeedDetail/${post.id}")
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            ProfileNickName(
                nickName = post.author.nickname,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 24.dp),
                isIconVisible = true,
            ) {
                //share
                val shareLink = "https://naenio.shop/posts/${post.id}"

                val type = "text/plain"
                val subject = "네니오로 오세요~~~"
                val extraText = shareLink
                val shareWith = "ShareWith"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)

                ContextCompat.startActivity(
                    context,
                    Intent.createChooser(intent, shareWith),
                    null
                )
            }
            VoteContent(post, Modifier, 2)
            VoteBar(
                post = post,
                onVote = onVote
            )
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

@Composable
fun FeedEmptyLayout(color : Color =  MyColors.darkGrey_828282) {
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
            color = color
        )
    }
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