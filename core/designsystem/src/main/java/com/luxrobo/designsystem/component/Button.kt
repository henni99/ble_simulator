package com.luxrobo.designsystem.component

import android.content.res.Configuration
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luxrobo.designsystem.theme.LuxColor.Blue01
import com.luxrobo.designsystem.theme.LuxTheme

/**
 * SpringyButton
 *
 * 스프링 애니메이션이 적용된 커스텀 버튼 컴포저블.
 * 버튼이 눌렸을 때 크기가 살짝 줄어들며, 손을 떼면 원래 크기로 부드럽게 복귀합니다.
 * MotionEvent를 직접 감지하여 눌림/취소/해제 상태를 제어하고,
 * onClick 동작은 ACTION_UP 시점에 실행됩니다.
 *
 * @param modifier       외부에서 전달받는 Modifier
 * @param enabled        버튼 활성화 여부
 * @param containerColor 버튼 배경 색상
 * @param contentColor   버튼 내부 콘텐츠(텍스트/아이콘) 색상
 * @param text           버튼에 표시될 텍스트
 * @param onClick        버튼 클릭 시 실행할 람다
 */

@Composable
fun SpringyButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Blue01,
    contentColor: Color = Color.White,
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = "scaleAnim"
    )

    Button(
        modifier = modifier
            .scale(scale)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                    }

                    MotionEvent.ACTION_UP -> {
                        isPressed = false
                        onClick()
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                    }
                }
                true
            },
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = {}
    ) {
        Text(
            style = LuxTheme.typography.bodyLargeR,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp),
            text = text,
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
private fun SpringyButtonPreview() {
    LuxTheme {
        SpringyButton(text = "기기 탐색", onClick = {})
    }
}
