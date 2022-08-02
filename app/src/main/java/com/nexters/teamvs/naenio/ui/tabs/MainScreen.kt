package com.nexters.teamvs.naenio.ui.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nexters.teamvs.naenio.graphs.MainNavGraph
import com.nexters.teamvs.naenio.theme.MyColors
import com.nexters.teamvs.naenio.theme.MyShape
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.dialog.SheetLayout
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    var currentBottomSheet: BottomSheetType? by remember { mutableStateOf(null) }

    if (!modalBottomSheetState.isVisible)
        currentBottomSheet = null

    val closeSheet: () -> Unit = {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    val openSheet: (BottomSheetType) -> Unit = {
        coroutineScope.launch {
            currentBottomSheet = it
            modalBottomSheetState.show()
        }
    }

//    BackHandler {
//        if (!modalBottomSheetState.isVisible) {
//            closeSheet.invoke()
//        } else {
//            navController.popBackStack()
//        }
//    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = MyShape.TopRoundedCornerShape,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet, closeSheet)
            }
            Spacer(modifier = Modifier.height(1.dp).background(Color.Transparent)) //content 비어있으면 error 발생으로 추가
        }
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) {
            MainNavGraph(
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = { openSheet.invoke(it) },
                closeSheet = { closeSheet.invoke() }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Feed,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }

    if (bottomBarDestination) {
        BottomNavigation(modifier = Modifier
            .fillMaxWidth()
            .height(bottomBarHeight)) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        selectedContentColor = MyColors.PrimaryColor,
        unselectedContentColor = MyColors.DarkGrey,
        onClick = {
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let { screen_route ->
                    popUpTo(screen_route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    MainScreen(rememberNavController())
}

val bottomBarHeight = 60.dp