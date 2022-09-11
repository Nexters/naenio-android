package com.nexters.teamvs.naenio.graphs

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.nexters.teamvs.naenio.ui.feed.detail.DetailScreen
import com.nexters.teamvs.naenio.ui.feed.detail.FeedDetail
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import com.nexters.teamvs.naenio.ui.tabs.auth.LoginScreen
import com.nexters.teamvs.naenio.ui.tabs.auth.setting.ProfileSettingScreen
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(
            route = AuthScreen.Login.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://{type}" }
            )
        ) {
            if (AuthDataStore.authToken.isNullOrEmpty()) {
                LoginScreen(
                    navController = navController,
                    viewModel = hiltViewModel(),
                    onNickName = {
                        navController.popBackStack()
                        navController.navigate(AuthScreen.ProfileSetting.route)
                    },
                    onNext = {
                        navController.popBackStack()
                        navController.navigate(Graph.MAIN)
                    }
                )
                it.arguments?.getString("type")?.let {
                    Log.d("#### deepLink", "login failed")
                    LoginScreen(
                        navController = navController,
                        viewModel = hiltViewModel(),
                        onNickName = {
                            navController.popBackStack()
                            navController.navigate(AuthScreen.ProfileSetting.route)
                        },
                        onNext = {
                            navController.popBackStack()
                            navController.navigate("FeedDetail/$it")
                        }
                    )
                }
            } else {
                Log.d("#### deepLink", "login success")
                it.arguments?.getString("type")?.let {
                    navController.navigate("FeedDetail/$it")
                }
            }

        }
        composable(route = AuthScreen.ProfileSetting.route) {
            ProfileSettingScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                onClose = {
                    navController.popBackStack()
                }
            ) {
                navController.popBackStack()
                navController.navigate(BottomNavItem.Profile.route)
            }
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object ProfileSetting : AuthScreen(route = "ProfileSetting")
}