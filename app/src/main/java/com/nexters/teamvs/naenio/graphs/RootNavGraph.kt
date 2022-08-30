package com.nexters.teamvs.naenio.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.android.gms.auth.api.Auth
import com.nexters.teamvs.naenio.ui.tabs.MainScreen
import com.nexters.teamvs.naenio.utils.datastore.AuthDataStore

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    var route : String
    if (AuthDataStore.userJson.isNullOrEmpty() || AuthDataStore.authToken.isNullOrEmpty()) {
        route = Graph.AUTHENTICATION
    } else {
        route = Graph.MAIN
    }
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = route
    ) {
        authNavGraph(navController = navController)
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