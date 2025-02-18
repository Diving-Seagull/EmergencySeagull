package com.divingseagull.emergencyseagull.composable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
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
            painter = painterResource(R.drawable.logo_minitab),
            contentDescription = "logoForTab",
            modifier = Modifier.height(25.dp)
        )
    }
}

@Composable
fun GBNTab(title:String = "test", onClick: () -> Unit){
    Row(
        modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.btn_black_arrow),
            contentDescription = "GBN",
            modifier = Modifier
                .size(20.dp, 20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(400),
                color = Color(0xFF323439),
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.size(20.dp, 20.dp))
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
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

@Composable
fun TitleText(
    upperTextLine: String = "Upper TextLine", lowerTextLine: String = "Lower TextLine", padding: Dp = 28.dp
) {
    Text(
        text = upperTextLine,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding)
            .height(42.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        style = TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            letterSpacing = (-0.3).sp,
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        )
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding)
            .height(42.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        text = lowerTextLine,
        fontSize = 28.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        letterSpacing = (-0.3).sp,
    )
}


fun vectorDrawableToBitmap(@DrawableRes drawableResId: Int, context: Context): Bitmap {
    val drawable = AppCompatResources.getDrawable(context, drawableResId)
        ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    val bitmap = Bitmap.createBitmap(
        60,
        60,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}