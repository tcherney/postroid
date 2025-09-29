package com.tcherney.postroid

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room


enum class Routes(val value: String) {
    HOME("home"),
}


@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java, "database-name"
    ).build()
    NavHost(navController = navController, startDestination = Routes.HOME.value) {
        composable(route = Routes.HOME.value) {
            Home(
                modifier = modifier,
                navController = navController
            )
        }
    }
}