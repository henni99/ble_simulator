package com.luxrobo.device_list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.luxrobo.ble_simulator.feature.device_list.R
import com.luxrobo.designsystem.theme.LuxTheme
import com.luxrobo.device_list.viewModel.BleDeviceListIntent


@Composable
fun BasicDialog(
    isDialogTitle: String,
    onDismissRequest: (BleDeviceListIntent) -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest(BleDeviceListIntent.DismissDialog)
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = isDialogTitle,
                    style = LuxTheme.typography.titleLargeB,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onDismissRequest(BleDeviceListIntent.DismissDialog) }
                ) {
                    Text(
                        text = stringResource(R.string.common_confirm),
                        style = LuxTheme.typography.bodyLargeR
                    )
                }
            }
        }
    }
}