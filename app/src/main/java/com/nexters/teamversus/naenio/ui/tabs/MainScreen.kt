package com.nexters.teamversus.naenio.ui.tabs

import android.annotation.SuppressLint
import android.util.Log
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
import com.nexters.teamversus.naenio.base.GlobalUiEvent
import com.nexters.teamversus.naenio.base.UiEvent
import com.nexters.teamversus.naenio.graphs.MainNavGraph
import com.nexters.teamversus.naenio.theme.MyColors
import com.nexters.teamversus.naenio.theme.MyShape
import com.nexters.teamversus.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamversus.naenio.ui.dialog.SheetLayout
import com.nexters.teamversus.naenio.ui.model.BottomNavItem
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        Log.d("### destination" , "$destination $arguments")
        coroutineScope.launch {
            GlobalUiEvent.uiEvent.emit(UiEvent.HideLoading)
        }
    }

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

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavItem.Theme,
        BottomNavItem.Feed,
        BottomNavItem.Profile,
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }

    if (bottomBarDestination) {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomBarHeight),
            backgroundColor = MyColors.black141414
        ) {
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
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    BottomNavigationItem(
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                tint = if (selected) Color.Unspecified else MyColors.grey8d95a0,
                contentDescription = "Navigation Icon"
            )
        },
        selected = selected,
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
//    MainScreen(rememberNavController())
}

val bottomBarHeight = 60.dp