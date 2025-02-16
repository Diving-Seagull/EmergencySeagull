package com.divingseagull.emergencyseagull.view

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.divingseagull.emergencyseagull.R
import com.divingseagull.emergencyseagull.composable.AudioPlayer
import com.divingseagull.emergencyseagull.composable.AudioRecorder
import com.divingseagull.emergencyseagull.composable.ClassificationTab
import com.divingseagull.emergencyseagull.composable.CommonButton
import com.divingseagull.emergencyseagull.composable.LogoTab
import com.divingseagull.emergencyseagull.ui.theme.pretendard
import com.divingseagull.emergencyseagull.viewModel.VM
import java.io.File


@Composable
fun SubSelectPage(navController: NavHostController, vm: VM) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        LogoTab()
        Text(
            text = buildAnnotatedString {
                append("신고자님!\n")
                withStyle(style = SpanStyle(color = Color(0xFFD51713))) {
                    append("신고 유형")
                }
                append("을 선택해주세요")
            },
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 34.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(700),
                color = Color(0xFF323439), // 기본 텍스트 색상
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(top = 24.dp, bottom = 18.dp)
        )
        Row(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_fireextinguisher,
                title = "화재 신고",
                description = "건축물, 자동차, 선박화재",
                onClick = {}
            )
            Spacer(modifier = Modifier.width(12.dp))
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_cone,
                title = "구조 신고",
                description = "일반 인명, 수중, 특수 구조",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_siren,
                title = "구급 신고",
                description = "현장응급, 생활응급 사고",
                onClick = {}
            )
            Spacer(modifier = Modifier.width(12.dp))
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_aidkit,
                title = "생활안전 신고",
                description = "일반 기타 사고",
                onClick = {}
            )
        }
        Column(
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 24.dp),
        ) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = 0,
                title = "바로 신고",
                description = "신고 분류가 어렵다면 이곳을 선택해주세요!",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "이용에 어려움이 있으신가요?",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500),
                color = Color(0xFFAFB8C1),
                textDecoration = TextDecoration.Underline,
            )
        )
        Spacer(modifier = Modifier.height(49.dp))
    }
}

/**
 * @author gykim_kr - Diving Seagull
 * @param navController Navigation
 * @param vm ViewModel
 * @suppress Main View Kotlin 파일
 * */
@Composable
fun MainPage(navController: NavHostController, vm: VM) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        LogoTab()
        Text(
            text = buildAnnotatedString {
                append("현재 상황에 맞는\n")
                withStyle(style = SpanStyle(color = Color(0xFFD51713))) {
                    append("신고 방법")
                }
                append("을 선택해주세요")
            },
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 34.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(700),
                color = Color(0xFF323439), // 기본 텍스트 색상
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(top = 24.dp, bottom = 18.dp)
        )
        Row(modifier = Modifier.height(159.dp).padding(start = 24.dp, end = 24.dp, top = 20.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_msg,
                title = "텍스트 신고",
                description = "소리내기 어려운 상황이라면 텍스트로 작성해주세요!",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.height(159.dp).padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_voice,
                title = "음성 신고",
                description = "텍스트 입력이 어렵다면 음성으로 신고 가능해요!",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "이용에 어려움이 있으신가요?",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500),
                color = Color(0xFFAFB8C1),
                textDecoration = TextDecoration.Underline,
            )
        )
        Spacer(modifier = Modifier.height(49.dp))
    }
}

/**
 * @author gykim_kr - Diving Seagull
 * @param navController Navigation
 * @param vm ViewModel
 * @param recorder AndroidAudioRecorder
 * @param player AndroidAudioPlayer
 * @suppress Audio Recording Test
 * */
@Composable
fun AudioRecordingPage(
    navController: NavHostController,
    vm: VM,
    recorder: AudioRecorder,
    player: AudioPlayer
) {
    val context = LocalContext.current
    var audioFile: File? = null

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CommonButton(text = "녹음 시작", onClick = {
            File(context.cacheDir, "audio.mp3").also {
                recorder.start(it)
                audioFile = it
            }
        })
        CommonButton(text = "녹음 종료", onClick = {
            recorder.stop()
            audioFile?.let { file ->
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        "audio_${System.currentTimeMillis()}.mp3"
                    )
                    put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
                    put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
                    put(MediaStore.Audio.Media.IS_PENDING, 1) // 임시 상태
                }

                val audioUri =
                    resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

                audioUri?.let { uri ->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    contentValues.clear()
                    contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0) // 저장 완료
                    resolver.update(uri, contentValues, null, null)
                }
            }
        })

        CommonButton(
            text = "재생 시작",
            onClick = { audioFile?.let { player.playFile(it) } }
        )
        CommonButton(text = "재생 정지", onClick = { player.stop() })
    }
}


@Preview
@Composable
fun SubSelectPagePreview() {
    SubSelectPage(navController = NavHostController(LocalContext.current), VM())
}

@Preview
@Composable
fun MainPagePreview() {
    MainPage(navController = NavHostController(LocalContext.current), VM())
}



