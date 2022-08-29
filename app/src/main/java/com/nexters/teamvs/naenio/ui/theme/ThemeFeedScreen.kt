package com.nexters.teamvs.naenio.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.feed.FeedEmptyLayout
import com.nexters.teamvs.naenio.ui.feed.FeedPager
import com.nexters.teamvs.naenio.ui.feed.composables.TopBar
import com.nexters.teamvs.naenio.utils.ShareUtils

/**
 * 테마 아이템 클릭 후 테마 피드 화면
 */
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ThemeFeedScreen(
    viewModel: ThemeFeedViewModel = hiltViewModel(),
    type: String,
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {

    val posts = viewModel.posts.collectAsState()
    val currentTheme by remember { // TODO ViewModel Runtime Injection 방식으로 변경
        mutableStateOf((ThemeItem.themeList.find { it.type.name == type }!!))
    }

    val isEmptyTheme = posts.value != null && posts.value?.isEmpty() == true

    LaunchedEffect(key1 = Unit, block = {
        if (posts.value.isNullOrEmpty()) viewModel.getThemePosts(currentTheme.type)
    })

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    if (isEmptyTheme) {
        FeedEmptyLayout(Color.White)
    } else {
        ThemeFeedContent(
            posts = posts.value ?: emptyList(),
            theme = currentTheme,
            close = {
                navController.popBackStack()
            },
            vote = { postId, choiceId ->
                viewModel.vote(postId = postId, choiceId = choiceId)
            },
            navController = navController,
            openSheet = openSheet,
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ThemeFeedContent(
    posts: List<Post>,
    theme: ThemeItem,
    navController: NavHostController,
    openSheet: (BottomSheetType) -> Unit,
    close: () -> Unit,
    vote: (Int, Int) -> Unit,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(theme.backgroundColorList))
    ) {
        TopBar(
            modifier = Modifier.padding(bottom = 20.dp),
            barTitle = theme.title,
            close = close,
            isMoreBtnVisible = false,
            textStyle = Font.pretendardSemiBold22
        )
        FeedPager(
            modifier = Modifier,
            posts = posts,
            pagerState = pagerState,
            openSheet = openSheet,
            navController = navController,
            onVote = vote,
            loadNextPage = { },
            onShare = {
                ShareUtils.share(it, context)
            }
        )
    }
}