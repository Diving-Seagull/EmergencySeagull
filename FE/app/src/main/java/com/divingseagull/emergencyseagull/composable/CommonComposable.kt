package com.divingseagull.emergencyseagull.composable

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divingseagull.emergencyseagull.ui.theme.pretendard

/* 공용으로 사용하는 Composable을 모아놓은 .Kotlin 파일. */

/**
 * @author gykim_kr - Diving Seagull
 * @param context Toast 출력 용
 * @suppress 오디오 권한 용
 * @see Main
 */

val buttonModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 24.dp, vertical = 16.dp)
    .height(58.dp)

@Composable
fun CommonButton(
    mModifier: Modifier = buttonModifier,
    text: String = "다음",
    buttonColor: Color = Color(0xFF397CDB),
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = mModifier,
        shape = RoundedCornerShape(size = 16.dp),
        colors = ButtonDefaults.buttonColors(buttonColor),
    ) {
        Text(
            text = text,
            fontFamily = pretendard,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}