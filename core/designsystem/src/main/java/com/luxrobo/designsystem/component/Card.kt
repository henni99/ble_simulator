package com.luxrobo.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxTheme

/**
 * BasicCard
 *
 * 모서리가 둥근 기본 카드 UI 컴포저블.
 * Surface를 사용하여 배경색, 콘텐츠 색, 그림자, 모양 등을 설정하며,
 * 기본적으로 가로폭을 가득 채우는 형태입니다.
 *
 * @param modifier      외부에서 전달받는 Modifier
 * @param color         카드 배경 색상 (기본: MaterialTheme.surface)
 * @param contentColor  카드 내부 콘텐츠 색상 (기본: MaterialTheme.onSurface)
 * @param content       카드 내부에 표시할 컴포저블 콘텐츠
 */

@Composable
fun BasicCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = color,
        contentColor = contentColor,
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp,
        content = content,
    )
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun BasicCardPreview() {
    LuxTheme {
        BasicCard(modifier = Modifier.size(320.dp, 160.dp), content = { })
    }
}
