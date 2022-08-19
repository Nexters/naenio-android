package com.nexters.teamvs.naenio.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.graphs.Graph
import com.nexters.teamvs.naenio.graphs.detailsNavGraph
import com.nexters.teamvs.naenio.graphs.themeDetailNavGraph
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.feed.FeedViewModel
import com.nexters.teamvs.naenio.ui.model.BottomNavItem

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ThemeScreen(
    navController: NavHostController,
    modifier: Modifier,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
            .wrapContentSize(Alignment.Center)
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 19.dp, start = 20.dp, bottom = 24.dp),
            text = stringResource(id = R.string.theme_title),
            fontSize = 24.sp,
            color = Color.White
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(ThemeItem.themeList) {
                ThemeGridItem(themeItem = it) { themeItem ->
                    var route : String
                    if (themeItem.type == "RANDOM_PLAY") {
                        Log.d("### ThemeScreen", "랜덤 투표 테마")
                        route = "FeedDetail/random=${themeItem.id}"
                    } else {
                        Log.d("### ThemeScreen", "랜덤 투표 외 테마")
                        route = "ThemeDetail/theme=${themeItem.id}"
                    }
                    navController.navigate(route)
                }
            }
        }
//        Text(
//            text = "Go to LoginScreen",
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .clickable {
//                    navController.popBackStack()
//                    navController.navigate(Graph.AUTHENTICATION)
//                },
//            textAlign = TextAlign.Center,
//            fontSize = 20.sp
//        )
    }
}

@Composable
fun ThemeGridItem(themeItem: ThemeItem, onClick: (ThemeItem) -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = themeItem.backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .height(182.dp)
            .clickable {
                onClick.invoke(themeItem)
            },
    ) {
        Image(
            modifier = Modifier.align(Alignment.BottomEnd),
            painter = painterResource(id = themeItem.image),
            contentDescription = ""
        )

        Text(
            modifier = Modifier.padding(14.dp),
            text = themeItem.description,
            color = Color.White,
            style = Font.pretendardMedium18
        )

        Text(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp, bottom = 24.dp)
                .align(Alignment.BottomStart),
            text = themeItem.title,
            color = Color.White,
            style = Font.pretendardSemiBold22
        )
    }
}