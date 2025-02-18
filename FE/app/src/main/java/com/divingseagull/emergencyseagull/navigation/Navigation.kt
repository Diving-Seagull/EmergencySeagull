package com.divingseagull.emergencyseagull.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.divingseagull.emergencyseagull.composable.AndroidAudioPlayer
import com.divingseagull.emergencyseagull.composable.AndroidAudioRecorder
import com.divingseagull.emergencyseagull.view.AudioReportPage
import com.divingseagull.emergencyseagull.view.EndingPage
import com.divingseagull.emergencyseagull.view.KakaoMapPage
import com.divingseagull.emergencyseagull.view.MainPage
import com.divingseagull.emergencyseagull.view.SplashPage
import com.divingseagull.emergencyseagull.view.TextReportPage
import com.divingseagull.emergencyseagull.viewModel.VM

/* Navigation 관리 */

/**
 * @author gykim_kr - Diving Seagull
 * @return nothing
 * @suppress Navigation 관리용 .kt 파일
 * */
@Composable
fun NavGraph(recorder: AndroidAudioRecorder, player: AndroidAudioPlayer) {
    val navController = rememberNavController()
    var vm = VM()
    NavHost(
        navController = navController,
        startDestination = "SplashPage"
    ) {
        //Main.kt
        composable("SplashPage") {
            SplashPage(navController, vm)
        }
        composable("MainPage") {
            MainPage(navController, vm)
        }
        composable("AudioReportPage") {
            AudioReportPage(navController, vm, recorder, player)
        }
        composable("TextReportPage") {
            TextReportPage(navController, vm)
        }
        composable("KakaoMapPage"){
            KakaoMapPage(navController, vm = vm)
        }
        composable("EndingPage"){
            EndingPage(navController)
        }
    }
}