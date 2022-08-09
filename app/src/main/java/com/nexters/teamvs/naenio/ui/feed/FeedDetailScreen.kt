package com.nexters.teamvs.naenio.ui.feed

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedDetailScreen(
    navController: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
    modifier: Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.naenio_confetti))

    val backHandler = BackHandler {
        if (modalBottomSheetState.isVisible) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    Box( modifier = Modifier
        .fillMaxSize()) {
        FeedDetail(modifier, backHandler)
        LottieAnimation(
            composition,
            modifier = Modifier.wrapContentSize(),
            iterations = Int.MAX_VALUE
        )
    }
}

@Composable
fun FeedDetail(modifier: Modifier, backHandler : Unit) {
    Column(modifier = modifier
        .wrapContentWidth()
        .background(MyColors.screenBackgroundColor)
        ) {
        TopBar(modifier = Modifier.wrapContentHeight(), barTitle = "", backHandler)
        Column(modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxHeight()) {
            ProfileNickName(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 32.dp), false)
            VoteContent(Modifier.padding(top = 24.dp), 4)
            Spacer(modifier = Modifier.fillMaxHeight(0.044f))
            VoteGageBar(0.5f, true)
            Spacer(modifier = Modifier.height(32.dp))
            CommentLayout(
                Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(
                        Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp)
                    .shadow(
                        1.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = MyColors.blackShadow_35000000
                    ))
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun FeedDetailScreen() {
//    FeedDetailScreen(
//        navController = NavHostController(LocalContext.current),
//        modalBottomSheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden),
//        modifier = Modifier.padding(bottomBarHeight),
//        openSheet = {},
//        closeSheet = {},
//        viewModel = viewModel()
//    )
    FeedDetail(modifier = Modifier, BackHandler{})
}