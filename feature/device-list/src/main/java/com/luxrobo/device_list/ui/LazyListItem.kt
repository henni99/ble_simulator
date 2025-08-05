package com.luxrobo.device_list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luxrobo.ble_simulator.feature.device_list.R
import com.luxrobo.designsystem.component.BasicCard
import com.luxrobo.designsystem.theme.LuxTheme
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.model.BleDeviceConnection
import kotlin.ranges.contains

@Composable
fun BleDeviceConnection(
    uiState: BleDeviceConnection,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    BasicCard {
        Row {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(
                            when {
                                uiState.rssi >= -70 -> R.drawable.signal_strong
                                uiState.rssi in -80 until -70 -> R.drawable.signal_normal
                                else -> R.drawable.signal_week
                            }
                        ),
                        contentDescription = null
                    )

                    Text(
                        style = LuxTheme.typography.bodyLargeR,
                        text = uiState.rssi.toString()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        style = LuxTheme.typography.titleLargeB,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        text = uiState.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Text(
                        style = LuxTheme.typography.bodyLargeR,
                        color = Color.Gray,
                        text = uiState.deviceId,
                        overflow = TextOverflow.Ellipsis,
                        minLines = 2,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    modifier = Modifier
                        .testTag("연결 버튼"),
                    enabled = !uiState.isConnecting,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Green,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    onClick = {
                        handleIntent(BleDeviceListIntent.SelectDevice(deviceConnection = uiState))
                    }
                ) {
                    Text(
                        fontSize = 10.sp,
                        style = LuxTheme.typography.bodyLargeR,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.bluetooth_list_connecting_status)
                    )
                }
            }


        }

    }
}