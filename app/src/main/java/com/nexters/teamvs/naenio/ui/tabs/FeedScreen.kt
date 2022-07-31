package com.nexters.teamvs.naenio.ui.tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.nexters.teamvs.naenio.R
import com.nexters.teamvs.naenio.theme.MyShape
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.dialog.SheetLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(navController: NavHostController, modifier: Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    var currentBottomSheet: BottomSheetType? by remember { mutableStateOf(null) }

    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    val closeSheet: () -> Unit = {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    //TODO 두번 불려야 바텀시트가 open 되는 이슈가 있음.
    val openSheet: (BottomSheetType) -> Unit = {
        coroutineScope.launch {
            currentBottomSheet = it
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }

    BackHandler {
        if (!bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
            closeSheet.invoke()
        } else {
            navController.popBackStack()
        }
    }

    BottomSheetScaffold(
        modifier = modifier,
        sheetPeekHeight = 0.dp,
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = MyShape.TopRoundedCornerShape,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet, closeSheet)
            }
        }
    ) {
        FeedPager(openSheet)
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun FeedPager(
    openSheet: (BottomSheetType) -> Unit,
) {
    VerticalPager(
        count = 10,
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ) { page ->
        Box(
            Modifier
                .padding(top = 20.dp)
        ) {
            FeedItem(
                page = page,
                openSheet = openSheet
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedItem(page: Int, openSheet: (BottomSheetType) -> Unit) {
    var gage by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = 0, block = {
        while (gage < 1) {
            gage += 0.05f
            delay(10)
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Feed Screen $page",
            color = Color.White,
            fontSize = 17.sp
        )
        Button(onClick = { openSheet(BottomSheetType.Comment) }) {
            Text(text = "Open bottom sheet Comment")
        }
        Button(onClick = { openSheet(BottomSheetType.Menu) }) {
            Text(text = "Open bottom sheet Menu")
        }
        GageBar(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .height(59.dp),
            gageModifier = Modifier.height(59.dp),
            width = 300.dp,
            backgroundColor = Color.Gray,
            foregroundColor = Brush.horizontalGradient(
                listOf(
                    Color(0xffFD7D20),
                    Color(0xffFBE41A)
                )
            ),
            percent = gage,
        )
    }
}

@Composable
fun GageBar(
    modifier: Modifier,
    gageModifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Float,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(width)
    ) {
        Box(
            modifier = gageModifier
                .background(foregroundColor)
                .width(width * percent)
        )
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    FeedScreen(
        navController = NavHostController(LocalContext.current),
        modifier = Modifier.padding(bottomBarHeight)
    )
}