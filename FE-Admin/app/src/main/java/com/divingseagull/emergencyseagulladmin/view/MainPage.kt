package com.divingseagull.emergencyseagulladmin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.divingseagull.emergencyseagulladmin.R
import com.divingseagull.emergencyseagulladmin.composable.ClassificationTab
import com.divingseagull.emergencyseagulladmin.ui.theme.pretendard
import com.divingseagull.emergencyseagulladmin.viewModel.VM
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navController: NavController) {
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
        LaunchedEffect(Unit) {
            delay(1500)
            navController.navigate("MainPage") {
                popUpTo("SplashPage") { inclusive = true }
            }
        }
    }
}

@Composable
fun MainPage(navController: NavHostController, vm: VM) {
    var fireStation by remember { mutableStateOf("동래소방서") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(

        ){
            Row(
                modifier = Modifier
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFFE2E4EC),
                        shape = RoundedCornerShape(size = 12.dp)
                    )
                    .padding(0.75.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = Color(0xFFFAFAFB), shape = RoundedCornerShape(size = 12.dp))
                    .padding(start = 20.dp, top = 6.dp, end = 16.dp, bottom = 6.dp)
            ) {
                Text(
                    text = fireStation,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF686D78),
                    )
                )
            }
        }

        Text(
            text = buildAnnotatedString {
                append("관리자님!\n")
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