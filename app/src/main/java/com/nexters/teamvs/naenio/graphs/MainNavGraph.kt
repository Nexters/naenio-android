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
import com.nexters.teamvs.naenio.ui.create.CreateScreen
import com.nexters.teamvs.naenio.ui.dialog.CommentDialogModel
import com.nexters.teamvs.naenio.ui.feed.FeedScreen
import com.nexters.teamvs.naenio.ui.feed.detail.FeedCommentDetail
import com.nexters.teamvs.naenio.ui.feed.detail.FeedDetailScreen
import com.nexters.teamvs.naenio.ui.feed.detail.RandomScreen
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import com.nexters.teamvs.naenio.ui.profile.ProfileDetailScreen
import com.nexters.teamvs.naenio.ui.profile.ProfileScreen
import com.nexters.teamvs.naenio.ui.tabs.auth.LoginDetailScreen
import com.nexters.teamvs.naenio.ui.tabs.bottomBarHeight
import com.nexters.teamvs.naenio.ui.theme.ThemeFeedScreen
import com.nexters.teamvs.naenio.ui.theme.ThemeScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
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

        composable(route = "FeedDeepLinkDetail/{type}") {
            FeedCommentDetail(
                type = it.arguments?.getString("type").orEmpty(),
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
        detailsNavGraph(navController, modalBottomSheetState, openSheet, closeSheet)
        authNavGraph(navController)
        themeNavGraph(navController, modalBottomSheetState, openSheet, closeSheet)
        profileDetailNavGraph(navController)
        loginDetailNavGraph(navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.detailsNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit
) {
    navigation(
        route = Graph.DETAILS,
        startDestination = "FeedCommentDetail/{type}",
    ) {
        composable(route = "FeedCommentDetail/{type}") {
            FeedCommentDetail(
                type = it.arguments?.getString("type").orEmpty(),
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet,
                isInvokeOpenSheet = true
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
                navController = navController,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.themeNavGraph(
    navController: NavHostController,
    modalBottomSheetState: ModalBottomSheetState,
    openSheet: (CommentDialogModel) -> Unit,
    closeSheet: () -> Unit
) {
    navigation(
        route = Graph.THEME,
        startDestination = "ThemeFeed/{type}"
    ) {
        composable(route = "ThemeFeed/{type}") {
            val themeType = it.arguments?.getString("type")!!
            ThemeFeedScreen(
                type = themeType,
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }

        composable(route = "FeedDetail/{type}") {
            val themeType = it.arguments?.getString("type")!!
            FeedDetailScreen(
                type = themeType,
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }

        composable(route = "Random") {
            RandomScreen(
                navController = navController,
                modalBottomSheetState = modalBottomSheetState,
                openSheet = openSheet,
                closeSheet = closeSheet
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.loginDetailNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.LOGIN_DETAIL,
        startDestination = "LoginDetail/{type}"
    ) {
        composable(route = "LoginDetail/{type}") {
            it.arguments?.getString("type")?.let { type ->
                LoginDetailScreen(
                    type = type,
                    navController = navController
                )
            }
        }
    }
}

