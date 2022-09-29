package com.nexters.teamversus.naenio.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nexters.teamversus.naenio.R
import com.nexters.teamversus.naenio.theme.Font
import com.nexters.teamversus.naenio.theme.MyColors

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ThemeScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    modifier: Modifier,
) {
    val themeItems = themeViewModel.themeItems.collectAsState()

    val themeNavigator: (ThemeItem) -> Unit = {
        val route = if (it.type == ThemeType.RANDOM_PLAY) {
            "Random"
        } else {
            "ThemeFeed/${it.type.name}"
        }
        navController.navigate(route)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MyColors.screenBackgroundColor)
            .wrapContentSize(Alignment.Center)
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(id = R.string.theme_title),
            fontSize = 24.sp,
            color = Color.White
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(themeItems.value) {
                ThemeGridItem(
                    themeItem = it,
                    onClick = themeNavigator
                )
            }
        }
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