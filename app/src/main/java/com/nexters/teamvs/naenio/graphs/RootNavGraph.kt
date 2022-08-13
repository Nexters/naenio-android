package com.nexters.teamvs.naenio.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nexters.teamvs.naenio.ui.tabs.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.MAIN
    ) {
        authNavGraph(navController = navController)
        composable(route = Graph.MAIN) { MainScreen() }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
    const val DETAILS = "details_graph"
    const val THEME_DETAIL = "theme_detail_graph"
}