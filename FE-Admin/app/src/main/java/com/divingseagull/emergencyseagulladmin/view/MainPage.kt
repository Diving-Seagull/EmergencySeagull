package com.divingseagull.emergencyseagulladmin.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.divingseagull.emergencyseagulladmin.R
import com.divingseagull.emergencyseagulladmin.composable.ClassificationTab
import com.divingseagull.emergencyseagulladmin.composable.CommonButton
import com.divingseagull.emergencyseagulladmin.composable.DistrictBottomSheet
import com.divingseagull.emergencyseagulladmin.composable.ReportBox
import com.divingseagull.emergencyseagulladmin.composable.Topbar
import com.divingseagull.emergencyseagulladmin.composable.classificationMap
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavHostController, vm: VM) {
    val district by vm.district.collectAsState()

    var selectedDistrict by remember { mutableStateOf(district ?: "동래구") }
    var isClicked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Topbar(district = selectedDistrict, onClick = { isClicked = !isClicked })
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
            modifier = Modifier.padding(top = 26.dp, bottom = 20.dp)
        )
        Row(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_fireextinguisher,
                title = "화재 신고",
                description = "건축물, 자동차, 선박화재",
                onClick = {
                    vm.updateClassification("FIRE")
                    vm.updateDistrict(selectedDistrict)
                    navController.navigate("ReportPage")
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_cone,
                title = "구조 신고",
                description = "일반 인명, 수중, 특수 구조",
                onClick = {
                    vm.updateClassification("RESCUE")
                    vm.updateDistrict(selectedDistrict)
                    navController.navigate("ReportPage")
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_siren,
                title = "구급 신고",
                description = "현장응급, 생활응급 사고",
                onClick = {
                    vm.updateClassification("MEDICAL")
                    vm.updateDistrict(selectedDistrict)
                    navController.navigate("ReportPage")
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ClassificationTab(
                mModifier = Modifier.weight(1f),
                icon = R.drawable.ic_aidkit,
                title = "생활안전 신고",
                description = "일반 기타 사고",
                onClick = {
                    vm.updateClassification("SAFETY")
                    vm.updateDistrict(selectedDistrict)
                    navController.navigate("ReportPage")
                }
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
                title = "신고 처리",
                description = "신고별 분류를 변경할 수 있습니다",
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
        Spacer(modifier = Modifier.height(65.dp))

    }
    if (isClicked) {
        DistrictBottomSheet(
            onIsClicked = { isClicked = !isClicked },
            onClick = {
                selectedDistrict = it
                isClicked = false
                vm.updateDistrict(selectedDistrict)
            }
        )
    }
}

@Composable
fun ReportPage(navController: NavController, vm: VM) {
    var selectedDistrict by remember { mutableStateOf(vm.district.value.toString()) }
    var isClicked by remember { mutableStateOf(false) }
    val reports by vm.reports.collectAsState()

    LaunchedEffect(selectedDistrict) {
        vm.fetchReports(
            onSuccess = {},
            onError = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFB))
    ) {
        Topbar(district = selectedDistrict, onClick = { isClicked = !isClicked })
        Text(
            text = classificationMap[vm.classification.collectAsState().value.toString()]
                ?: "알 수 없음",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(700),
                color = Color(0xFF323439),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(start = 26.dp, top = 18.dp, end = 26.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reports) { report ->
                ReportBox(
                    specification = report.sub_category,
                    location = report.address,
                    description = report.content,
                    id = report.id,
                    vm = vm
                )
            }
        }
        Spacer(modifier = Modifier.height(65.dp))
    }
    if (isClicked) {
        DistrictBottomSheet(
            onIsClicked = { isClicked = !isClicked },
            onClick = {
                selectedDistrict = it
                isClicked = false
                vm.updateDistrict(selectedDistrict)
            }
        )
    }
}

@Preview
@Composable
fun MainPagePreview() {
    MainPage(navController = NavHostController(LocalContext.current), vm = VM())
}

@Preview
@Composable
fun ReportPagePreview() {
    ReportPage(navController = NavHostController(LocalContext.current), vm = VM())

}