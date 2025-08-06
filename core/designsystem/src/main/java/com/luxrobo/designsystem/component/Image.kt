package com.luxrobo.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource


@Composable
fun PainterImage(
    modifier: Modifier = Modifier,
    id: Int,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id),
        contentDescription = null
    )
}