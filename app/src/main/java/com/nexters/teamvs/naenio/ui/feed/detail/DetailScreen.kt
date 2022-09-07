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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamvs.naenio.ui.feed.FeedEmptyLayout
import com.nexters.teamvs.naenio.ui.feed.composables.*
import com.nexters.teamvs.naenio.ui.theme.ThemeItem
import com.nexters.teamvs.naenio.ui.theme.ThemeType
import com.nexters.teamvs.naenio.utils.ShareUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    type: String,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel(),
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
) {
    val context = LocalContext.current
    val postItem = viewModel.postItem.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))
    var isAnim by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.successVote.collect {
            isAnim = true
            delay(1000L)
            isAnim = false
        }
    })
    val modifier: Modifier
    var titleBar = ""
    var textStyle: TextStyle = Font.pretendardSemiBold16
    if (type == ThemeType.RANDOM_PLAY.name) {
        Log.d("### FeedDetailScreen", "Random")
        LaunchedEffect(key1 = Unit, block = {
            viewModel.getRandomPost()
        })
        titleBar = ThemeItem.themeList[2].title
        textStyle = Font.pretendardSemiBold22
        modifier =
            Modifier.background(Brush.verticalGradient(ThemeItem.themeList[2].backgroundColorList))
    } else {
        LaunchedEffect(key1 = Unit, block = {
            viewModel.getPostDetail(type.toInt())
        })
        Log.d("### FeedDetailScreen", "FeedDetail")
        modifier = Modifier.background(MyColors.screenBackgroundColor)
    }

    val isEmptyPost = postItem.value == null

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isEmptyPost) {
            FeedDetail(
                postItem.value!!,
                modifier,
                navController,
                titleBar,
                textStyle,
                viewModel = viewModel,
                openSheet = openSheet,
                onShare = {
                    ShareUtils.share(it, context)
                }
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
                    textStyle = textStyle,
                    post = null
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
                        viewModel.getRandomPost()
                    }
            )
        }
    }
}

@Composable
fun FeedDetail(
    post: Post,
    modifier: Modifier,
    navController: NavHostController,
    titleBar: String?,
    textStyle: TextStyle,
    viewModel: DetailViewModel,
    openSheet: (CommentDialogModel) -> Unit,
    onShare: (Int) -> Unit,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(
            modifier = Modifier.wrapContentHeight(),
            barTitle = titleBar,
            close = {
                navController.popBackStack()
            },
            isMoreBtnVisible = true,
            textStyle = textStyle,
            post = post
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
                onShare = { onShare.invoke(post.id) },
                onMore = {}
            )
            VoteContent(post = post, modifier = Modifier.padding(top = 24.dp), maxLine = 4)
            Spacer(modifier = Modifier.fillMaxHeight(0.044f))
            VoteBar(
                post = post,
                onVote = { postId, voteId ->
                    viewModel.vote(postId, voteId)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
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