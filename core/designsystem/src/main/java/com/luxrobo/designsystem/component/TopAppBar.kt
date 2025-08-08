package com.luxrobo.designsystem.component

import android.R
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxTheme

/**
 * BasicTopAppBar
 *
 * 상단 앱바 UI 컴포저블.
 * - 좌측에는 로고나 아이콘 버튼(`logoButtons`)을 배치
 * - 중앙에는 제목 텍스트(`titleRes`)를 표시
 * - 우측에는 액션 버튼(`actionButtons`)을 배치
 *
 * @param titleRes       제목에 사용할 문자열 리소스 ID
 * @param modifier       외부에서 전달받는 Modifier
 * @param containerColor 앱바 배경색
 * @param actionButtons  우측에 배치할 버튼 콘텐츠
 * @param logoButtons    좌측에 배치할 로고/아이콘 콘텐츠
 */

@Composable
fun BasicTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    actionButtons: @Composable () -> Unit = {},
    logoButtons: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RectangleShape,
                clip = false
            )
            .background(containerColor)
            .then(modifier)
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Row(Modifier.align(Alignment.CenterStart)) {
            logoButtons()
        }

        Row(Modifier.align(Alignment.CenterEnd)) {
            actionButtons()
        }

        Text(
            text = stringResource(id = titleRes),
            style = LuxTheme.typography.titleLargeB,
            maxLines = 1,
            minLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun BasicTopAppBarPreview() {
    BasicTopAppBar(
        titleRes = R.string.untitled,
    )
}
