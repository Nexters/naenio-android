package com.nexters.teamvs.naenio.ui.feed

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.domain.model.Post
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedDetailScreen(
    type: String = "feedDetail",
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    val postItem = viewModel.postItem.collectAsState()
    viewModel.setType(type)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))
    val themeItem = viewModel.themeItem.collectAsState()

    var modifier: Modifier
    var titleBar: String = ""
    var textStyle: TextStyle = Font.pretendardSemiBold16
    if (type.contains("random")) {
        Log.d("### FeedDetailScreen", "Random")
        titleBar = themeItem.value.title
        textStyle = Font.pretendardSemiBold22
        modifier = Modifier.background(Brush.verticalGradient(themeItem.value.backgroundColorList))
    } else {
        Log.d("### FeedDetailScreen", "FeedDetail")
        modifier = Modifier.background(MyColors.screenBackgroundColor)
    }

    BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FeedDetail(postItem.value, modifier, navController, titleBar, textStyle)
        LottieAnimation(
            composition, modifier = Modifier.wrapContentSize(), iterations = Int.MAX_VALUE
        )
        if (type.contains("random")) {
            Image(painter = painterResource(id = R.drawable.ic_random),
                contentDescription = "ic_random",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 28.dp)
                    .clickable {
                        viewModel.getRandomPost()
                    })
        }
    }
}

@Composable
fun FeedDetail(
    post: Post?,
    modifier: Modifier,
    navController: NavHostController,
    titleBar: String?,
    textStyle: TextStyle
) {
    post?.let { post ->
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            TopBar(Modifier.wrapContentHeight(), titleBar, navController, true, textStyle)
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
                    isIconVisible = false
                )
                VoteContent(post = post, modifier = Modifier.padding(top = 24.dp), maxLine = 4)
                Spacer(modifier = Modifier.fillMaxHeight(0.044f))
                VoteBar(post)
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
                )
            }
        }
    }
}