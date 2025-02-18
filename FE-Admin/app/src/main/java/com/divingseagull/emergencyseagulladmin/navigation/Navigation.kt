package com.divingseagull.emergencyseagulladmin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.divingseagull.emergencyseagulladmin.view.AcceptedPage
import com.divingseagull.emergencyseagulladmin.view.EndingPage
import com.divingseagull.emergencyseagulladmin.view.FinalPage
import com.divingseagull.emergencyseagulladmin.view.MainPage
import com.divingseagull.emergencyseagulladmin.view.ReportPage
import com.divingseagull.emergencyseagulladmin.view.SplashPage
import com.divingseagull.emergencyseagulladmin.viewModel.VM

@Composable
fun NavGraph(){
    val navController = rememberNavController()
    var vm = VM()
    NavHost(
        navController = navController,
        startDestination = "SplashPage"
    ){
        composable("SplashPage") {
            SplashPage(navController)
        }
        composable("MainPage") {
            MainPage(navController, vm)
        }
        composable("ReportPage") {
            ReportPage(navController, vm)
        }
        composable("AcceptedPage") {
            AcceptedPage(navController, vm)
        }
        composable("FinalPage") {
            FinalPage( navController)
        }
        composable("EndingPage") {
            EndingPage(navController)
        }
    }
}