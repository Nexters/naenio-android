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
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.nexters.teamvs.naenio.ui.create.CreateScreen
import com.nexters.teamvs.naenio.ui.dialog.BottomSheetType
import com.nexters.teamvs.naenio.ui.feed.detail.DetailScreen
import com.nexters.teamvs.naenio.ui.feed.FeedScreen
import com.nexters.teamvs.naenio.ui.tabs.*
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import com.nexters.teamvs.naenio.ui.theme.ThemeScreen
import com.nexters.teamvs.naenio.ui.profile.ProfileDetailScreen
import com.nexters.teamvs.naenio.ui.profile.ProfileScreen
import com.nexters.teamvs.naenio.ui.theme.ThemeFeedContent
import com.nexters.teamvs.naenio.ui.theme.ThemeFeedScreen

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
        startDestination = BottomNavItem.Feed.route
    ) {
        val modifier = Modifier.padding(bottom = bottomBarHeight)
        composable(BottomNavItem.Theme.route) {
            ThemeScreen(
                navController = navController,
                modifier = modifier,
            )
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(navController = navController, modifier = modifier)
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
        composable(Route.Create) {
            CreateScreen(
                navController = navController
            )
        }

        composable(route = "FeedDetail/{type}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://{type}" }
            )
        ) {
            DetailScreen(
                type = it.arguments?.getString("type").orEmpty(),
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
//        detailsNavGraph(navController, modalBottomSheetState, openSheet, closeSheet)
        authNavGraph(navController)
        themeDetailNavGraph(navController, modalBottomSheetState, openSheet, closeSheet)
        profileDetailNavGraph(navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.detailsNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit
) {
    navigation(
        route = Graph.DETAILS,
        startDestination = "FeedDetail/{type}",
    ) {
        composable(route = "FeedDetail/{type}") {
            DetailScreen(
                type = it.arguments?.getString("type").orEmpty(),
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.profileDetailNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.PROFILE_DETAIL,
        startDestination = "ProfileDetail/{profileType}"
    ) {
        composable(route = "ProfileDetail/{profileType}") {
            ProfileDetailScreen(
                profileType = it.arguments?.getString("profileType").orEmpty(),
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.themeDetailNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (BottomSheetType) -> Unit,
    closeSheet: () -> Unit
) {
    navigation(
        route = Graph.THEME_DETAIL,
        startDestination = "ThemeDetail/{type}"
    ) {
        composable(route = "ThemeDetail/{type}") {
            val themeType = it.arguments?.getString("type")!!
            ThemeFeedScreen(
                type = themeType,
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
    }
}