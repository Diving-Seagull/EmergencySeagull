package com.divingseagull.emergencyseagull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.divingseagull.emergencyseagull.navigation.NavGraph
import com.divingseagull.emergencyseagull.ui.theme.EmergencySeagullTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmergencySeagullTheme {
                NavGraph()
            }
        }
    }
}