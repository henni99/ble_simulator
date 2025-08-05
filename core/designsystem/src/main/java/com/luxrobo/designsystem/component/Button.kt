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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxColor.Blue01
import com.luxrobo.designsystem.theme.LuxTheme

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
            style = LuxTheme.typography.titleLargeB,
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
