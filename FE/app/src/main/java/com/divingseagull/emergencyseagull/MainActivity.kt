package com.divingseagull.emergencyseagull

import android.Manifest
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.divingseagull.emergencyseagull.composable.AndroidAudioPlayer
import com.divingseagull.emergencyseagull.composable.AndroidAudioRecorder
import com.divingseagull.emergencyseagull.navigation.NavGraph
import com.divingseagull.emergencyseagull.ui.theme.EmergencySeagullTheme
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk

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
            arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            0
        )
        enableEdgeToEdge()
        setContent {
            Log.d("KeyHash", "${Utility.getKeyHash(this)}")

            val kakaoMapKey = BuildConfig.KAKAO_MAP_KEY
            KakaoSdk.init(this, kakaoMapKey)
            KakaoMapSdk.init(this, kakaoMapKey);
            EmergencySeagullTheme {
                NavGraph(recorder, player)
            }
        }
    }
}