package com.nexters.teamversus.naenio.ui.feed

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.base.GlobalUiEvent
import com.nexters.teamversus.naenio.base.NaenioApp
import com.nexters.teamversus.naenio.data.network.dto.ReportType
import com.nexters.teamversus.naenio.domain.model.Post
import com.nexters.teamversus.naenio.extensions.noRippleClickable
import com.nexters.teamversus.naenio.extensions.requireActivity
import com.nexters.teamversus.naenio.graphs.Route
import com.nexters.teamversus.naenio.theme.Font
import com.nexters.teamversus.naenio.theme.MyColors
import com.nexters.teamversus.naenio.ui.component.MenuDialogModel
import com.nexters.teamversus.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamversus.naenio.ui.feed.composables.CommentLayout
import com.nexters.teamversus.naenio.ui.feed.composables.ProfileNickName
import com.nexters.teamversus.naenio.ui.feed.composables.VoteBar
import com.nexters.teamversus.naenio.ui.feed.composables.VoteContent
import com.nexters.teamversus.naenio.utils.DimensionUtils
import com.nexters.teamversus.naenio.utils.ShareUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        if (viewModel.posts.value.isNullOrEmpty()) viewModel.loadFirstFeed()
    })

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            /**
             * 백스택이 비어있는 상태에서 popBackStack() 호출 시, 흰색 스크린만 보이는 상태가 되는 이슈가 있어서,
             * 액티비티를 finish 시킴.
             * TODO 처리 방법 추가 검토 필요
             */
            if (navController.previousBackStackEntry == null) {
                context.requireActivity().finish()
            } else {
                navController.popBackStack()
            }
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
    openSheet: (CommentDialogModel) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    /**
     * FeedScreenContent 에서만 필요한 State 이기 때문에 해당 컴포저블 내에서 상태를 갖도록 함.
     * 테마에서는 아래 상태들을 알 필요가 없음.
     */
    val feedTabItems = viewModel.feedTabItems.collectAsState()
    val selectedTab = viewModel.selectedTab.collectAsState()
    val posts = viewModel.posts.collectAsState()
    val user = viewModel.user.collectAsState(null)
    val isEmptyFeed = posts.value != null && posts.value?.isEmpty() == true

    val pagerState = rememberPagerState(initialPage = 0)

    var isAnim by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.event.collect {
            when (it) {
                FeedEvent.ScrollToTop -> {
                    pagerState.scrollToPage(0)
                }
                is FeedEvent.VoteSuccess -> {
                    isAnim = it.id
                    delay(1000L)
                    isAnim = null
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
            FeedTopBar(
                text = stringResource(id = R.string.bottom_item_feed),
                onRefresh = {
                    viewModel.refresh()
                }
            )

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
                    bottomPadding = if (NaenioApp.isShortScreen) 0.dp else 100.dp,
                    posts = posts.value ?: emptyList(),
                    isAnim = isAnim,
                    openSheet = openSheet,
                    onVote = { postId, choiceId ->
                        viewModel.vote(postId = postId, choiceId = choiceId)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    onShare = { post ->
                        ShareUtils.share(post = post, context = context)
                    },
                    loadNextPage = viewModel::loadNextPage,
                    onDetail = {
                        viewModel.setDetailPostItem(it)
                        navController.navigate("FeedDetail/$it")
                    },
                    onMore = {
                        if (it.author.id == user.value?.id) {
                            scope.launch {
                                GlobalUiEvent.showMenuDialog(
                                    listOf(
                                        MenuDialogModel(
                                            text = "사용자 차단하기",
                                            onClick = {
                                                viewModel.block(userId = it.author.id)
                                            }
                                        ),
                                        MenuDialogModel(
                                            text = "삭제하기",
                                            onClick = {
                                                viewModel.deletePost(postId = it.id)
                                            }
                                        )
                                    )
                                )
                            }
                        } else {
                            scope.launch {
                                GlobalUiEvent.showMenuDialog(
                                    listOf(
                                        MenuDialogModel(
                                            text = "사용자 차단하기",
                                            onClick = {
                                                viewModel.block(
                                                    userId = it.author.id,
                                                )
                                            }
                                        ),
                                        MenuDialogModel(
                                            text = "게시물 신고하기",
                                            onClick = {
                                                viewModel.report(
                                                    targetMemberId = it.author.id,
                                                    resourceType = ReportType.POST
                                                )
                                            }
                                        )
                                    )
                                )
                            }
                        }
                    }
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
fun FeedTopBar(text: String, onRefresh: () -> Unit) {
    Row {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 10.dp),
            text = text,
            style = Font.montserratSemiBold24,
            color = Color.White
        )
        Image(
            modifier = Modifier
                .padding(20.dp)
                .size(28.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    onRefresh.invoke()
                },
            imageVector = Icons.Filled.Refresh,
            contentDescription = "",
            colorFilter = ColorFilter.tint(color = Color.White),
        )
    }
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
    bottomPadding: Dp = 100.dp,
    posts: List<Post>,
    pagerState: PagerState,
    isAnim: Int?,
    openSheet: (CommentDialogModel) -> Unit,
    onVote: (Int, Int) -> Unit,
    onDetail: (Int) -> Unit,
    onShare: (Post) -> Unit,
    loadNextPage: () -> Unit,
    onMore: (Post) -> Unit,
) {
    val threshold = 3
    val lastIndex = posts.lastIndex
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))

    VerticalPager(
        state = pagerState,
        count = posts.size,
        itemSpacing = 20.dp,
        contentPadding = PaddingValues(bottom = bottomPadding),
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
                post = posts[page],
                onDetail = onDetail,
                onVote = onVote,
                openSheet = openSheet,
                onMore = onMore,
                onShare = onShare
            )
            AnimatedVisibility(
                visible = isAnim == posts[page].id,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.wrapContentSize(),
                    iterations = LottieConstants.IterateForever
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedItem(
    post: Post,
    onDetail: (Int) -> Unit,
    onVote: (Int, Int) -> Unit,
    openSheet: (CommentDialogModel) -> Unit,
    onMore: (Post) -> Unit,
    onShare: (Post) -> Unit,
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
            onDetail.invoke(post.id)
        },
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
                onShare = { onShare.invoke(post) },
                onMore = { onMore.invoke(post) },
                profileImageIndex = post.author.profileImageIndex
            )
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
                    openSheet(CommentDialogModel(post.id, totalCommentCount = post.commentCount))
                }
        )
    }
}

@Composable
fun FeedEmptyLayout(color: Color = MyColors.darkGrey_828282) {
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