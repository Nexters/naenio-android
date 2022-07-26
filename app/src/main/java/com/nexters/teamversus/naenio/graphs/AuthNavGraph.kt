package com.nexters.teamversus.naenio.graphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.nexters.teamversus.naenio.ui.tabs.DetailScreen
import com.nexters.teamversus.naenio.ui.tabs.auth.LoginScreen
import com.nexters.teamversus.naenio.ui.tabs.auth.NicknameScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            LoginScreen (
                viewModel = hiltViewModel(),
                onNickName = {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Nickname.route)
                },
                onNext = {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN)
                }
            )
        }
        composable(route = AuthScreen.Nickname.route) {
            NicknameScreen(viewModel = hiltViewModel()) {
                navController.popBackStack()
                navController.navigate(Graph.MAIN)
            }
        }
        composable(route = AuthScreen.Forgot.route) {
            DetailScreen(name = AuthScreen.Forgot.route) {}
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object Nickname : AuthScreen(route = "Nickname")
    object Forgot : AuthScreen(route = "FORGOT")
}