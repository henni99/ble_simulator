package com.luxrobo.device_list.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxColor.Blue01

@Composable
fun ScanningRippleEffect(
    modifier: Modifier = Modifier,
    rippleColor: Color = Blue01,
    circleCount: Int = 3
) {
    val infiniteTransition = rememberInfiniteTransition()

    val ripples = List(circleCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    delayMillis = index * 400,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        contentAlignment = Alignment.CenterStart, // ← 여기만 바꿔주면 됨
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(0f + size.height / 2, size.height / 2)
            val maxRadius = size.minDimension / 2

            ripples.forEach { ripple ->
                val alpha = 1f - ripple.value
                drawCircle(
                    color = rippleColor.copy(alpha = alpha),
                    radius = ripple.value * maxRadius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }


    }
}