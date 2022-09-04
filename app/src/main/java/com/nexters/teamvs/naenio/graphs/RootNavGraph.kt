package com.nexters.teamvs.naenio.graphs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nexters.teamvs.naenio.ui.tabs.MainScreen
import com.nexters.teamvs.naenio.ui.tabs.auth.LoginScreen
import com.nexters.teamvs.naenio.ui.tabs.auth.setting.ProfileSettingScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("### destination" , "$destination $arguments")
        }
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
                    navController.navigate(AuthScreen.Login.route)
                }
            ) {
                navController.popBackStack()
                navController.navigate(Graph.MAIN)
            }
        }
        composable(
            route = Graph.MAIN
        ) { MainScreen() }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
    const val DETAILS = "details_graph"
    const val THEME_DETAIL = "theme_detail_graph"
    const val PROFILE_DETAIL = "profile_detail_graph"
    const val LOGIN_DETAIL = "login_detail_graph"
}