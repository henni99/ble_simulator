package com.luxrobo.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SansSerifStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Bold,
)

internal val Typography = LuxTypography(
    titleLargeB = SansSerifStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    bodyLargeR = SansSerifStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
)

@Immutable
data class LuxTypography(
    val titleLargeB: TextStyle,
    val bodyLargeR: TextStyle
)

val LocalTypography = staticCompositionLocalOf {

    LuxTypography(
        titleLargeB = SansSerifStyle,
        bodyLargeR = SansSerifStyle
    )

}
