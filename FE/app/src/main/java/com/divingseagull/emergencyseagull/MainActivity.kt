package com.divingseagull.emergencyseagull

import android.Manifest
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.divingseagull.emergencyseagull.composable.AndroidAudioPlayer
import com.divingseagull.emergencyseagull.composable.AndroidAudioRecorder
import com.divingseagull.emergencyseagull.navigation.NavGraph
import com.divingseagull.emergencyseagull.ui.theme.EmergencySeagullTheme

class MainActivity : ComponentActivity() {
    private val recorder by lazy {
        AndroidAudioRecorder(this)
    }
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )
        enableEdgeToEdge()
        setContent {
            EmergencySeagullTheme {
                NavGraph(recorder, player)
            }
        }
    }
}