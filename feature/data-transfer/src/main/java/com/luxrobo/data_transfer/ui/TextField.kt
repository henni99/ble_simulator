package com.luxrobo.data_transfer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxTheme

@Composable
fun ScrollableReadOnlyTextField(
    modifier: Modifier = Modifier,
    title: String,
    text: String
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(text) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Text(
        text = title,
        style = LuxTheme.typography.titleLargeB
    )

    Spacer(Modifier.height(8.dp))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // 둥근 테두리
            .background(Color.White) // 배경색
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // 테두리
            .padding(12.dp) // 내부 패딩
            .verticalScroll(scrollState)
    ) {
        BasicTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
    }

}

@Composable
fun MessageInputRow(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (text.isNotBlank()) {
                        onSend(text)
                        onTextChange("")
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                    }
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        IconButton(
            modifier = Modifier.height(40.dp),
            onClick = {
                if (text.isNotBlank()) {
                    onSend(text)
                    onTextChange("")
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "전송"
            )
        }
    }
}
