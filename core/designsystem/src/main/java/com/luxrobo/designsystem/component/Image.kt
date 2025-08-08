package com.luxrobo.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * PainterImage
 *
 * 리소스 ID를 받아 해당 이미지를 그리는 간단한 래퍼 컴포저블.
 * 내부적으로 painterResource를 사용하여 Drawable 리소스를 불러옵니다.
 *
 * @param modifier  외부에서 전달받는 Modifier
 * @param id        표시할 이미지 리소스 ID
 */

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