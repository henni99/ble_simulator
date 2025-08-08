package com.luxrobo.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * VerticalSpacer
 *
 * 지정한 높이만큼 세로 간격을 주는 Spacer 컴포저블.
 *
 * @param height 세로 간격 값 (Dp)
 */

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(
        modifier = Modifier.height(height)
    )
}

/**
 * HorizontalSpacer
 *
 * 지정한 너비만큼 가로 간격을 주는 Spacer 컴포저블.
 *
 * @param width 가로 간격 값 (Dp)
 */

@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(
        modifier = Modifier.width(width)
    )
}