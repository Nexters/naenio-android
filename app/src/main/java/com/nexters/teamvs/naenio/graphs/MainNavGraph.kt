package com.nexters.teamvs.naenio.graphs

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.feed.FeedScreen
import com.nexters.teamvs.naenio.ui.tabs.*
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import com.nexters.teamvs.naenio.ui.home.HomeScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit,
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomNavItem.Theme.route
    ) {
        val modifier = Modifier.padding(bottom = bottomBarHeight)
        composable(BottomNavItem.Theme.route) {
            HomeScreen(navController = navController, modifier = modifier)
        }
        composable(BottomNavItem.Feed.route) {
            FeedScreen(
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                modifier = modifier,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController = navController, modifier = modifier)
        }
        detailsNavGraph(navController)
        authNavGraph(navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {
        composable(route = DetailsScreen.Information.route) {
            DetailScreen(name = DetailsScreen.Information.route) {
                navController.navigate(DetailsScreen.Overview.route)
            }
        }
        composable(route = DetailsScreen.Overview.route) {
            DetailScreen(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}
