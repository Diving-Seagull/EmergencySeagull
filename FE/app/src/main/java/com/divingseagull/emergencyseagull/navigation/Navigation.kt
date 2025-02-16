package com.divingseagull.emergencyseagull.navigation

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.divingseagull.emergencyseagull.composable.AndroidAudioPlayer
import com.divingseagull.emergencyseagull.composable.AndroidAudioRecorder
import com.divingseagull.emergencyseagull.view.AudioRecordingPage
import com.divingseagull.emergencyseagull.view.MainPage
import com.divingseagull.emergencyseagull.viewModel.VM
import java.io.File

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
        startDestination = "AudioRecordingPage"
    ) {
        //Main.kt
        composable("MainPage") {
            MainPage(navController, vm)
        }
        composable("AudioRecordingPage") {
            AudioRecordingPage(navController, vm, recorder, player)
        }
    }
}