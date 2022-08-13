package com.nexters.teamvs.naenio.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.Font
import com.nexters.teamvs.naenio.theme.MyColors

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier) {

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
            items(ThemeItem.mock) {
                ThemeGridItem(themeItem = it) { themeId ->
                    if (themeId == 123) { //랜텀투표면
                        //todo navigate detail
                    } else {
                        //TODO navigate feed
                    }
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
fun ThemeGridItem(themeItem: ThemeItem, onClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = themeItem.backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .height(182.dp)
            .clickable {
                onClick.invoke(themeItem.id)
            },
    ) {
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

        Image(
            modifier = Modifier.align(Alignment.BottomEnd),
            painter = painterResource(id = themeItem.image),
            contentDescription = ""
        )
    }
}