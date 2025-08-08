package com.luxrobo.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = LuxColor.White,
    onPrimary = LuxColor.Blue01,
    primaryContainer = LuxColor.Graphite,
    onPrimaryContainer = LuxColor.White,
    inversePrimary = LuxColor.Blue02,
    secondary = LuxColor.Blue04,
    onSecondary = LuxColor.Blue01,
    secondaryContainer = LuxColor.Blue04,
    onSecondaryContainer = LuxColor.LightWhite,
    surfaceContainerLow = LuxColor.LightWhite,
    tertiary = LuxColor.Yellow05,
    onTertiary = LuxColor.Yellow01,
    tertiaryContainer = LuxColor.Yellow04,
    onTertiaryContainer = LuxColor.White,
    error = LuxColor.Red02,
    onError = LuxColor.Red05,
    errorContainer = LuxColor.Red04,
    onErrorContainer = LuxColor.Red01,
    surface = LuxColor.Graphite,
    onSurface = LuxColor.White,
    onSurfaceVariant = LuxColor.White,
    surfaceVariant = LuxColor.White,
    surfaceDim = LuxColor.Black,
    surfaceContainerHigh = LuxColor.DuskGray,
    inverseSurface = LuxColor.Neon05,
    inverseOnSurface = LuxColor.Black,
    outline = LuxColor.DarkGray,
    outlineVariant = LuxColor.Cosmos,
    scrim = LuxColor.Black,
    surfaceContainerLowest = LuxColor.Graphite,
)


private val LightColorScheme = lightColorScheme(
    primary = LuxColor.Neon01,
    onPrimary = LuxColor.White,
    primaryContainer = LuxColor.White,
    onPrimaryContainer = LuxColor.Graphite,
    inversePrimary = LuxColor.Neon01,
    secondary = LuxColor.Blue04,
    onSecondary = LuxColor.White,
    secondaryContainer = LuxColor.Blue01,
    onSecondaryContainer = LuxColor.LightBlack,
    surfaceContainerLow = LuxColor.Blue01,
    tertiary = LuxColor.Yellow01,
    onTertiary = LuxColor.Black,
    tertiaryContainer = LuxColor.Yellow03A40,
    onTertiaryContainer = LuxColor.Yellow04,
    error = LuxColor.Red03,
    onError = LuxColor.White,
    errorContainer = LuxColor.Red01,
    onErrorContainer = LuxColor.Red06,
    surface = LuxColor.White,
    onSurface = LuxColor.Black,
    onSurfaceVariant = LuxColor.DarkGray,
    surfaceVariant = LuxColor.Graphite,
    surfaceDim = LuxColor.PaleGray,
    surfaceContainerHigh = LuxColor.LightGray,
    inverseSurface = LuxColor.Yellow05,
    inverseOnSurface = LuxColor.White,
    outline = LuxColor.Gainsboro,
    outlineVariant = LuxColor.DarkGray,
    scrim = LuxColor.Black,
    surfaceContainerLowest = LuxColor.PaleGray,
)

@Composable
fun LuxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (!LocalInspectionMode.current) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

object LuxTheme {
    val typography: LuxTypography
        @Composable
        get() = LocalTypography.current
}