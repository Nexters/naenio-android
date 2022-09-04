package com.nexters.teamvs.naenio.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nexters.teamvs.naenio.ui.model.BottomNavItem
import com.nexters.teamvs.naenio.ui.tabs.auth.LoginScreen
import com.nexters.teamvs.naenio.ui.tabs.auth.setting.ProfileSettingScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
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