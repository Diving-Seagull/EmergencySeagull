package com.divingseagull.emergencyseagull.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.divingseagull.emergencyseagull.R
import com.divingseagull.emergencyseagull.composable.AndroidAudioPlayer
import com.divingseagull.emergencyseagull.composable.AndroidAudioRecorder
import com.divingseagull.emergencyseagull.composable.AudioPlayer
import com.divingseagull.emergencyseagull.composable.AudioRecorder
import com.divingseagull.emergencyseagull.composable.ClassificationTab
import com.divingseagull.emergencyseagull.composable.CommonButton
import com.divingseagull.emergencyseagull.composable.GBNTab
import com.divingseagull.emergencyseagull.composable.LogoTab
import com.divingseagull.emergencyseagull.composable.TitleText
import com.divingseagull.emergencyseagull.composable.vectorDrawableToBitmap
import com.divingseagull.emergencyseagull.ui.theme.pretendard
import com.divingseagull.emergencyseagull.viewModel.VM
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.animation.Interpolation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.PolygonStyles
import com.kakao.vectormap.shape.PolygonStylesSet
import com.kakao.vectormap.shape.ShapeAnimator
import com.kakao.vectormap.shape.animation.CircleWave
import com.kakao.vectormap.shape.animation.CircleWaves
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashPage(navController: NavController, vm: VM) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFAF1310)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.logo),
            contentDescription = "Splash Image"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Emergency_911",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(590),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )

        val context = LocalContext.current
        var showPermissionDialog by remember {
            mutableStateOf(false)
        }
        val locationProviderClient = remember {
            LocationServices.getFusedLocationProviderClient(context)
        }
        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionState = rememberMultiplePermissionsState(permissions = permissions)
        val allRequiredPermission =
            permissionState.revokedPermissions.none { it.permission in permissions.first() }

        if (allRequiredPermission) {
            LaunchedEffect(Unit) {
                val priority = if (permissionState.permissions
                        .filter { it.status.isGranted }
                        .map { it.permission }
                        .contains(Manifest.permission.ACCESS_FINE_LOCATION)
                ) {
                    Priority.PRIORITY_HIGH_ACCURACY
                } else {
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY
                }

                try {
                    val result = locationProviderClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token
                    ).await()

                    result?.let { fetchedLocation ->
                        Log.d(
                            "location",
                            "lat: ${fetchedLocation.latitude}, long: ${fetchedLocation.longitude}"
                        )
                        vm.updateLocation(fetchedLocation.latitude, fetchedLocation.longitude)
                    }
                } catch (e: Exception) {
                    Log.e("LocationError", "신고자 현 GPS 위치 정보 획득 실패: ${e.message}")
                }
                delay(1500)
                navController.navigate("MainPage") {
                    popUpTo("SplashPage") { inclusive = true }
                }
            }
        }
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
        Row(
            modifier = Modifier
                .height(159.dp)
                .padding(start = 24.dp, end = 24.dp, top = 20.dp)
        ) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_msg,
                title = "텍스트 신고",
                description = "소리내기 어려운 상황이라면 텍스트로 작성해주세요!",
                onClick = {
                    navController.navigate("TextReportPage")
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .height(159.dp)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_voice,
                title = "음성 신고",
                description = "텍스트 입력이 어렵다면 음성으로 신고 가능해요!",
                onClick = {
                    navController.navigate("AudioReportPage")
                }
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


@Composable
fun TextReportPage(navController: NavHostController, vm: VM) {
    var text by remember { mutableStateOf("") }
    var textHeightValue by remember { mutableStateOf(42.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        GBNTab(title = "텍스트 신고", onClick = {
            navController.navigate("MainPage") {
                popUpTo("ReportPage") { inclusive = true }
            }
        })
        Spacer(modifier = Modifier.height(34.dp))
        Text(
            text = "신고 상황을 알려주세요",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(600),
                color = Color(0xFF323439),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier =
            if (textHeightValue <= 183.dp) {
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(183.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE1E2E6),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color(0xFFFAFAFB),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(top = 16.dp, bottom = 16.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE1E2E6),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(
                        color = Color(0xFFFAFAFB),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(top = 16.dp, bottom = 15.dp)

            },
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        if (it.length <= 500) {
                            text = it
                        }
                    },
                    cursorBrush = SolidColor(Color.Blue),
                    keyboardOptions = KeyboardOptions.Default,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusable(),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color(0xFF686D78),
                        fontFamily = pretendard,
                        fontWeight = FontWeight(600)
                    ),
                    onTextLayout = { textLayoutResult ->

                        val lineCount = textLayoutResult.lineCount
                        Log.d("LineCount", "Line Count: $lineCount")
                        var calculatedHeight = (lineCount * 24).dp

                        textHeightValue = maxOf(183.dp, calculatedHeight)


                    },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 18.dp, end = 10.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "위치를 작성해주시면 빠른 출동에 도움이 됩니다.",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color(0xFFAFB8C1),
                                        fontFamily = pretendard,
                                        fontWeight = FontWeight(500),
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.align(Alignment.TopStart)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CommonButton(
            text = "위치 지정하기",
            buttonColor = if (text.isEmpty()) Color(0xFFFAC6C5) else Color(0xFFD51713),
            onClick = {
                if (text.isNotEmpty()) {
                    navController.navigate("KakaoMapPage") {
                        popUpTo("ReportPage") { inclusive = true }
                    }
                }

            }

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
fun AudioReportPage(
    navController: NavHostController,
    vm: VM,
    recorder: AudioRecorder,
    player: AudioPlayer
) {
    val context = LocalContext.current
    var audioFile: File? = null
    var isRecording = false// 녹음 상태 관리
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun startAnimation() {
        scope.launch {
            while (isRecording) { // isRecording이 true일 때만 실행
                scale.animateTo(
                    targetValue = 3f,
                    animationSpec = tween(800, easing = LinearEasing)
                )
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(800, easing = LinearEasing)
                )
                scale.snapTo(0.5f)
                alpha.snapTo(0.3f)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        GBNTab(title = "음성 신고", onClick = {
            navController.navigate("MainPage") {
                popUpTo("ReportPage") { inclusive = true }
            }
        })
        Spacer(modifier = Modifier.height(80.dp))
        TitleText("듣고 있어요", "지금 말씀해주세요")
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "“ 여기 장전동 64번지 골목인데 불이 났어요! ”",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(600),
                color = Color(0xFF949BA8),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "“ 광안리 바닷가에 사람이 빠졌어요! ”",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(600),
                color = Color(0xFF949BA8),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(R.drawable.ic_recordbutton),
                contentDescription = "recording",
                modifier = Modifier
                    .zIndex(1f)
                    .clickable {
                        if (!isRecording) {
                            File(context.cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }
                        } else {
                            recorder.stop()
                            vm.updateAudioFile(audioFile)
                            val audio = vm.audioFile.value
                            audio.let { file ->
                                val resolver = context.contentResolver
                                val contentValues = ContentValues().apply {
                                    put(
                                        MediaStore.Audio.Media.DISPLAY_NAME,
                                        "audio_${System.currentTimeMillis()}.mp3"
                                    )
                                    put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
                                    put(
                                        MediaStore.Audio.Media.RELATIVE_PATH,
                                        Environment.DIRECTORY_MUSIC
                                    )
                                    put(MediaStore.Audio.Media.IS_PENDING, 1) // 임시 상태
                                }

                                val audioUri =
                                    resolver.insert(
                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        contentValues
                                    )

                                audioUri?.let { uri ->
                                    if (file != null) {
                                        resolver
                                            .openOutputStream(uri)
                                            ?.use { outputStream ->
                                                file
                                                    .inputStream()
                                                    .use { inputStream ->
                                                        inputStream.copyTo(outputStream)
                                                    }
                                            }
                                    }

                                    contentValues.clear()
                                    contentValues.put(MediaStore.Audio.Media.IS_PENDING, 0) // 저장 완료
                                    resolver.update(uri, contentValues, null, null)
                                }
                            }
                        }
                        startAnimation()
                        isRecording = !isRecording
                    }
            )
            Canvas(modifier = Modifier.size(50.dp)) {
                drawCircle(
                    color = Color(0xFFF69F9E).copy(alpha = alpha.value),
                    radius = size.minDimension / 2 * scale.value
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
        Spacer(modifier = Modifier.weight(1f))
        CommonButton(
            text = "위치 지정하기",
            onClick = {
                navController.navigate("KakaoMapPage") {
                    popUpTo("ReportPage") { inclusive = true }
                }
            }
        )
        Spacer(modifier = Modifier.height(50.dp))
    }
}


//@OptIn(ExperimentalPermissionsApi::class)
//@RequiresPermission(
//    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION]
//)
//@Composable
//fun LocationPage(
//    navController: NavController
//) {
//    val context = LocalContext.current
//    var showPermissionDialog by remember {
//        mutableStateOf(false)
//    }
//    val locationProviderClient = remember {
//        LocationServices.getFusedLocationProviderClient(context)
//    }
//    val permissions = listOf(
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    )
//    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
//    val allRequiredPermission =
//        permissionState.revokedPermissions.none { it.permission in permissions.first() }
//
//    if (allRequiredPermission) {
//        LocationButton(
//            locationProviderClient = locationProviderClient,
//            userPreciseLocation =
//            permissionState.permissions
//                .filter { it.status.isGranted }
//                .map { it.permission }
//                .contains(Manifest.permission.ACCESS_FINE_LOCATION)
//        )
//    } else {
//        Button(
//            onClick = { showPermissionDialog = true }
//        ) {
//            Text(text = "Click")
//        }
//    }
//
//    if (showPermissionDialog) {
//        LocationDialog(permissionState = permissionState) {
//            showPermissionDialog = it
//        }
//    }
//}

@Composable
fun KakaoMapPage(
    navController: NavController,
    vm: VM
) {
    val latitude = vm.latitude.value ?: 0.0
    val longitude = vm.longitude.value ?: 0.0
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(alpha = 0f))
                    .zIndex(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 62.dp)
                        .padding(horizontal = 24.dp)
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFFE2E4EC),
                            shape = RoundedCornerShape(size = 12.dp)
                        )
                        .padding(1.5.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(size = 12.dp))
                        .padding(start = 22.dp, top = 14.dp, end = 22.dp, bottom = 14.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_textfield_search),
                        contentDescription = "search"
                    )
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        cursorBrush = SolidColor(Color.Blue),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (text.isNotEmpty()) {

                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusable(),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = Color(0xFF121212),
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Normal
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 20.dp)
                            ) {
                                if (text.isEmpty()) {
                                    Text(
                                        text = "주소를 입력해주세요.",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color(0xFF949BA8),
                                            fontFamily = pretendard,
                                            fontWeight = FontWeight.Normal
                                        ),
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFFCDD1DB),
                            shape = RoundedCornerShape(size = 1000.dp)
                        )
                        .padding(1.5.dp)
                        .height(31.dp)
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(size = 1000.dp)
                        )
                        .padding(start = 16.dp, top = 8.dp, end = 20.dp, bottom = 8.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_siren),
                        contentDescription = "location"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "현 위치로 신고하기",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight(600),
                            color = Color(0xFF323439),
                        )
                    )
                }
                CommonButton(
                    text = "신고하기",
                    onClick = {
                        navController.navigate("EndingPage") {
                            popUpTo("KakaoMapPage") { inclusive = true }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(50.dp))
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    mapView.apply {
                        mapView.start(
                            object : MapLifeCycleCallback() {
                                override fun onMapDestroy() {
                                    Log.d("KakaoMap", "지도를 불러오는데 실패했습니다.")
                                }

                                override fun onMapError(exception: Exception?) {
                                    Log.d(
                                        "KakaoMap",
                                        "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception"
                                    )
                                }
                            },
                            object : KakaoMapReadyCallback() {
                                // KakaoMap이 준비되었을 때 호출
                                override fun onMapReady(kakaoMap: KakaoMap) {
                                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(
                                        LatLng.from(
                                            latitude,
                                            longitude
                                        )
                                    )
                                    Log.d("kakaoCameraUpdate", "OK")

//                                    // 지도에 표시할 라벨의 스타일 설정
//                                    val style = kakaoMap.labelManager?.addLabelStyles(
//                                        LabelStyles.from(
//                                            LabelStyle.from(
//                                                vectorDrawableToBitmap(
//                                                    R.drawable.ic_circle,
//                                                    context
//                                                )
//                                            )
//                                        )
//                                    )
//                                    Log.d("kakaoLabelStyle", "OK")
//
//                                    // 라벨 옵션을 설정하고 위치와 스타일을 적용
//                                    val options =
//                                        LabelOptions.from(LatLng.from(latitude, longitude))
//                                            .setStyles(style)
//                                    Log.d("kakaoLabelLatLng", "OK")
//
//                                    // KakaoMap의 labelManager에서 레이어를 가져옴
//                                    val layer = kakaoMap.labelManager?.layer
//                                    Log.d("kakaoLayer", "OK")
//
//                                    // 카메라를 지정된 위치로 이동
//                                    kakaoMap.moveCamera(cameraUpdate)
//                                    Log.d("kakaoCameraMove", "OK")
//
//                                    // 지도에 라벨을 추가
//                                    layer?.addLabel(options)
//                                    Log.d("kakaoLabelAdd", "OK")

                                    // 중심 라벨 생성
                                    val centerLabel = kakaoMap.labelManager?.layer?.addLabel(
                                        LabelOptions.from(
                                            "dotLabel",
                                            LatLng.from(latitude, longitude)
                                        )
                                            .setStyles(
                                                LabelStyle.from(
                                                    vectorDrawableToBitmap(
                                                        R.drawable.ic_circle,
                                                        context
                                                    )
                                                ).setAnchorPoint(0.5f, 0.5f)
                                            )
                                            .setRank(1)
                                    )

// 애니메이션 폴리곤 생성
                                    val animationPolygon = kakaoMap.shapeManager?.layer?.addPolygon(
                                        PolygonOptions.from("circlePolygon")
                                            .setDotPoints(
                                                DotPoints.fromCircle(
                                                    LatLng.from(
                                                        latitude,
                                                        longitude
                                                    ), 1.0f
                                                )
                                            )
                                            .setStylesSet(
                                                PolygonStylesSet.from(
                                                    PolygonStyles.from(Color(0xFFff722b).toArgb())
                                                )
                                            )
                                    )

                                    val circleWaves: CircleWaves = CircleWaves.from(
                                        "circleWaveAnim",
                                        CircleWave.from(1F, 0F, 0F, 100F)
                                    )
                                        .setHideShapeAtStop(false)
                                        .setInterpolation(Interpolation.CubicInOut)
                                        .setDuration(1500)
                                        .setRepeatCount(500)

                                    val shapeAnimator: ShapeAnimator? =
                                        kakaoMap.shapeManager?.addAnimator(circleWaves)
                                    shapeAnimator?.addPolygons(animationPolygon)
                                    shapeAnimator?.start()

                                }

                                override fun getPosition(): LatLng {
                                    // 현재 위치를 반환
                                    Log.d("returnPosition", LatLng.from(latitude, longitude).toString())
                                    return LatLng.from(latitude, longitude)

                                }
                            },
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun EndingPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = "ending"
        )
        Spacer(modifier = Modifier.height(37.dp))
        Text(
            text = "신고완료!",
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 40.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(700),
                color = Color(0xFFD51713),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "빠르게 출동하겠습니다",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(700),
                color = Color(0xFF949BA8),
                textAlign = TextAlign.Center,
            )
        )
        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate("MainPage")
        }
    }
}

@Preview
@Composable
fun SplashPagePreview() {
    SplashPage(navController = NavHostController(LocalContext.current), VM())
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

@Preview
@Composable
fun ReportPagePreview() {
    TextReportPage(navController = NavHostController(LocalContext.current), VM())
}

@Preview
@Composable
fun AudioReportPagePreview() {
    AudioReportPage(
        navController = NavHostController(LocalContext.current),
        VM(),
        recorder = AndroidAudioRecorder(LocalContext.current),
        player = AndroidAudioPlayer(LocalContext.current)
    )
}


