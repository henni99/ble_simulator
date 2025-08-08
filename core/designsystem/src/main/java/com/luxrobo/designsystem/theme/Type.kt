package com.luxrobo.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class LuxTypography(
    val titleLargeB: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    val bodyLargeR: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    val bodyMediumR: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    val bodySmallR: TextStyle = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )
)

internal val LocalTypography = staticCompositionLocalOf { LuxTypography() }