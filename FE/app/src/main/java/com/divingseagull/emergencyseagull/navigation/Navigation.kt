package com.divingseagull.emergencyseagull.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.divingseagull.emergencyseagull.view.MainPage
import com.divingseagull.emergencyseagull.viewModel.VM

/* Navigation 관리 */

/**
 * @author gykim_kr - Diving Seagull
 * @return nothing
 * @suppress Navigation 관리용 .kt 파일
* */
@Composable
fun NavGraph(){
    val navController = rememberNavController()
    var vm = VM()
    NavHost(
        navController = navController,
        startDestination = "MainPage"
    ) {
        //Main.kt
        composable("MainPage") {
            MainPage(navController, vm)
        }
    }
}