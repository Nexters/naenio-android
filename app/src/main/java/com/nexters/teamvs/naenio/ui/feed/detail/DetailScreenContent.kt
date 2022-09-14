package com.nexters.teamvs.naenio.ui.feed.detail

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.base.GlobalUiEvent
import com.nexters.teamvs.naenio.data.network.dto.ReportType
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.extensions.errorMessage
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.component.MenuDialogModel
import com.nexters.teamvs.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamvs.naenio.ui.feed.FeedEmptyLayout
import com.nexters.teamvs.naenio.ui.feed.FeedEvent
import com.nexters.teamvs.naenio.ui.feed.FeedViewModel
import com.nexters.teamvs.naenio.ui.feed.composables.*
import com.nexters.teamvs.naenio.ui.theme.ThemeItem
import com.nexters.teamvs.naenio.ui.theme.ThemeType
import com.nexters.teamvs.naenio.utils.ShareUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RandomScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val postItem = viewModel.postItem.collectAsState()
    var isAnim by remember { mutableStateOf(false) }
    val user = viewModel.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getRandomPost()
    })

    LaunchedEffect(key1 = Unit, block = {
        viewModel.successVote.collect {
            isAnim = true
            delay(1000L)
            isAnim = false
        }
    })

    DetailScreenContent(
        type = ThemeType.RANDOM_PLAY.name,
        navController = navController,
        modalBottomSheetState = modalBottomSheetState,
        openSheet = openSheet,
        closeSheet = closeSheet,
        postItem = postItem.value,
        onVote = { postId, voteId ->
            viewModel.vote(postId, voteId)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        isAnim = isAnim,
        onRefreshRandom = {
            viewModel.getRandomPost()
        },
        onMore = {
            if (it.author.id == user.value?.id) {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "삭제",
                            onClick = {
                                viewModel.deletePost(postId = it.id)
                            }
                        )
                    )
                }
            } else {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "신고",
                            onClick = {
                                viewModel.report(
                                    targetMemberId = it.author.id,
                                    resourceType = ReportType.POST
                                )
                            }
                        )
                    )
                }
            }
        },
        onShare = {
            ShareUtils.share(it, context = context)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedDetailScreen(
    type: String,
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
) {
    Log.d("### type", type)
    val backStackEntry = remember {
        navController.getBackStackEntry(
            navController.previousBackStackEntry?.destination?.route ?: ""
        )
    }
    val feedViewModel: FeedViewModel = hiltViewModel(backStackEntry)
    DetailScreen(
        feedViewModel = feedViewModel,
        type = type,
        navController = navController,
        modalBottomSheetState = modalBottomSheetState,
        openSheet = openSheet,
        closeSheet = closeSheet
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedCommentDetail(
    viewModel: DetailViewModel = hiltViewModel(),
    type: String,
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
    isInvokeOpenSheet: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    val postItem = viewModel.postItem.collectAsState()
    var isAnim by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user = viewModel.user.collectAsState(initial = null)
    LaunchedEffect(key1 = Unit, block = {
        Log.d("### FeedCommentDetail postId", type)
        try {
            GlobalUiEvent.showLoading()
            val post = viewModel.getPostDetail(type.toInt()) ?: return@LaunchedEffect
            if (isInvokeOpenSheet) {
                openSheet.invoke(
                    CommentDialogModel(postId = post.id, totalCommentCount = post.commentCount)
                )
            }
        } catch (e: Exception) {
            GlobalUiEvent.showToast(e.errorMessage())
        } finally {
            GlobalUiEvent.hideLoading()
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        viewModel.successVote.collect {
            isAnim = true
            delay(1000L)
            isAnim = false
        }
    })

    DetailScreenContent(
        type = type,
        navController = navController,
        modalBottomSheetState = modalBottomSheetState,
        openSheet = openSheet,
        closeSheet = closeSheet,
        isAnim = isAnim,
        isSingleDetail = !isInvokeOpenSheet,
        postItem = postItem.value,
        onVote = { postId, voteId ->
            viewModel.vote(postId, voteId)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        onMore = {
            if (it.author.id == user.value?.id) {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "삭제",
                            onClick = {
                                viewModel.deletePost(postId = it.id)
                            }
                        )
                    )
                }
            } else {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "신고",
                            onClick = {
                                viewModel.report(
                                    targetMemberId = it.author.id,
                                    resourceType = ReportType.POST
                                )
                            }
                        )
                    )
                }
            }
        },
        onShare = {
            ShareUtils.share(it, context = context)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    feedViewModel: FeedViewModel,
    type: String,
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
    isInvokeOpenSheet: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current

    val postItem = feedViewModel.postItem.collectAsState(null)
    val user = feedViewModel.user.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var isAnim by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        feedViewModel.event.collect {
            if (it is FeedEvent.VoteSuccess) {
                isAnim = true
                delay(1000L)
                isAnim = false
            }
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        postItem.value?.id?.let {
            feedViewModel.getPostDetail(it)
            postItem.value?.commentCount?.let { comment ->
                if (isInvokeOpenSheet) {
                    openSheet.invoke(
                        CommentDialogModel(
                            postId = it,
                            totalCommentCount = comment
                        )
                    )
                }
            }
        }

    })

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                feedViewModel.setDetailPostItem(null)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DetailScreenContent(
        type = type,
        navController = navController,
        modalBottomSheetState = modalBottomSheetState,
        openSheet = openSheet,
        closeSheet = closeSheet,
        postItem = postItem.value,
        onVote = { postId, voteId ->
            feedViewModel.vote(postId, voteId)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        isAnim = isAnim,
        onRefreshRandom = {
            feedViewModel.getRandomPost()
        },
        onShare = {
            ShareUtils.share(it, context)
        },
        onMore = {
            if (it.author.id == user.value?.id) {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "삭제",
                            onClick = {
                                feedViewModel.deletePost(postId = it.id)
                            }
                        )
                    )
                }
            } else {
                scope.launch {
                    GlobalUiEvent.showMenuDialog(
                        MenuDialogModel(
                            text = "신고",
                            onClick = {
                                feedViewModel.report(
                                    targetMemberId = it.author.id,
                                    resourceType = ReportType.POST
                                )
                            }
                        )
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun DetailScreenContent(
    type: String,
    navController: NavHostController,
    postItem: Post?,
    modalBottomSheetState: ModalBottomSheetState,
    onVote: (Int, Int) -> Unit,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
    isAnim: Boolean,
    onRefreshRandom: () -> Unit = {},
    onMore: (Post) -> Unit,
    onShare: (Post) -> Unit,
    isSingleDetail: Boolean = false
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))

    val modifier: Modifier
    var titleBar = ""
    var textStyle: TextStyle = Font.pretendardSemiBold16
    if (type == ThemeType.RANDOM_PLAY.name) {
        titleBar = ThemeItem.themeList[2].title
        textStyle = Font.pretendardSemiBold22
        modifier =
            Modifier.background(Brush.verticalGradient(ThemeItem.themeList[2].backgroundColorList))
    } else {
        Log.d("### FeedDetailScreen", "FeedDetail")
        modifier = Modifier.background(MyColors.screenBackgroundColor)
    }

    val isEmptyPost = postItem == null

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
            if (isSingleDetail) {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.MAIN) { inclusive = true }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isEmptyPost) {
            FeedDetail(
                postItem,
                modifier,
                navController,
                titleBar,
                textStyle,
                openSheet = openSheet,
                onShare = {
                    ShareUtils.share(it, context)
                },
                onVote = onVote,
                onMore = onMore
            )
            AnimatedVisibility(
                visible = isAnim,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier.wrapContentSize(),
                    iterations = LottieConstants.IterateForever
                )
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(
                    modifier = Modifier.wrapContentHeight(),
                    barTitle = titleBar,
                    close = {
                        navController.popBackStack()
                    },
                    isMoreBtnVisible = true,
                    isShareBtnVisible = true,
                    textStyle = textStyle,
                    post = null,
                    onMore = onMore,
                    onShare = onShare,
                )
                FeedEmptyLayout(Color.White)
            }
        }

        if (type == ThemeType.RANDOM_PLAY.name) {
            Image(painter = painterResource(id = R.drawable.ic_random),
                contentDescription = "ic_random",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 28.dp)
                    .clickable {
                        onRefreshRandom.invoke()
                    }
            )
        }
    }
}

@Composable
fun FeedDetail(
    post: Post?,
    modifier: Modifier,
    navController: NavHostController,
    titleBar: String?,
    textStyle: TextStyle,
    openSheet: (CommentDialogModel) -> Unit,
    onShare: (Post) -> Unit,
    onVote: (Int, Int) -> Unit,
    onMore: (Post) -> Unit,
) {
    post ?: return

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            TopBar(
                modifier = Modifier.wrapContentHeight(),
                barTitle = titleBar,
                close = {
                    navController.popBackStack()
                },
                isMoreBtnVisible = true,
                isShareBtnVisible = true,
                textStyle = textStyle,
                post = post,
                onMore = onMore,
                onShare = onShare
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxHeight()
            ) {
                ProfileNickName(
                    nickName = post.author.nickname.orEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 32.dp),
                    profileImageIndex = post.author.profileImageIndex,
                    isIconVisible = false,
                    onShare = { onShare.invoke(post) },
                    onMore = {}
                )
                VoteContent(post = post, modifier = Modifier.padding(top = 24.dp), maxLine = 4)
                Spacer(modifier = Modifier.fillMaxHeight(0.044f))
                VoteBar(
                    post = post,
                    onVote = onVote,
                    maxLine = 4
                )
                Spacer(modifier = Modifier.height(32.dp))
                CommentLayout(
                    commentCount = post.commentCount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(
                            Color.Black, shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp)
                        .shadow(
                            1.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = MyColors.blackShadow_35000000
                        )
                        .clickable {
                            openSheet.invoke(
                                CommentDialogModel(
                                    postId = post.id,
                                    totalCommentCount = post.commentCount
                                )
                            )
                        }
                )
            }
        }
    }
}