package com.tcherney.postroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class Routes(val value: String) {
    HOME("home"),
}


@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME.value) {
        composable(route = Routes.HOME.value) {
            Home(
                modifier = modifier,
                navController = navController
            )
        }
    }
}