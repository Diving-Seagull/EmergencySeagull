package com.divingseagull.emergencyseagull.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.divingseagull.emergencyseagull.R
import com.divingseagull.emergencyseagull.ui.theme.pretendard

/* Main.kt에서 사용하는 Composable을 모아놓은 .Kotlin 파일. */

@Composable
fun LogoTab(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_siren),
            contentDescription = "logo",
            modifier = Modifier.size(24.dp, 24.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_bell),
            contentDescription = "alarm"
        )
    }
}

@Composable
fun ClassificationTab(
    mModifier: Modifier,
    heightValue: Dp = 157.dp,
    icon: Int = R.drawable.ic_fireextinguisher,
    title: String = "Test",
    description: String = "Test",
    onClick: () -> Unit
) {
    Box(
        modifier = mModifier
            .shadow(
                elevation = 18.dp,
                spotColor = Color(0x0F000000),
                ambientColor = Color(0x0F000000)
            )
            .height(heightValue)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 14.dp))
            .padding(
                start = 16.dp,
                top = if (icon != 0) 20.dp else 0.dp,
                end = 16.dp,
                bottom = if (icon != 0) 20.dp else 0.dp
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if(icon != 0) Arrangement.Bottom else Arrangement.Center
        ) {
            if (icon != 0) {
                Image(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = "fireExtinguisher",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF323439),
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF858C9A),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
