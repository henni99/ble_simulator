package com.luxrobo.device_list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.luxrobo.designsystem.component.HorizontalSpacer
import com.luxrobo.designsystem.component.VerticalSpacer
import com.luxrobo.designsystem.theme.LuxColor.Blue01
import com.luxrobo.designsystem.theme.LuxColor.White
import com.luxrobo.designsystem.theme.LuxTheme
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.model.BleDeviceConnection

@Composable
fun BleDeviceConnection(
    uiState: BleDeviceConnection,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    BasicCard {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    style = LuxTheme.typography.titleLargeB,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    text = uiState.name,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                VerticalSpacer(4.dp)

                Text(
                    style = LuxTheme.typography.bodyLargeR,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    text = uiState.deviceId,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2,
                    maxLines = 2
                )
            }

            HorizontalSpacer(16.dp)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .testTag("연결 버튼")
                        .wrapContentHeight(),
                    enabled = !uiState.isConnecting,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = White,
                        containerColor = Blue01,
                        disabledContentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    onClick = {
                        handleIntent(BleDeviceListIntent.SelectDevice(deviceConnection = uiState))
                    }
                ) {
                    Text(
                        fontSize = 12.sp,
                        style = LuxTheme.typography.bodyLargeR,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.bluetooth_list_connecting_status)
                    )
                }

                VerticalSpacer(4.dp)

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            when {
                                uiState.rssi >= -70 -> R.drawable.signal_strong
                                uiState.rssi in -80 until -70 -> R.drawable.signal_normal
                                else -> R.drawable.signal_week
                            }
                        ),
                        contentDescription = null
                    )

                    HorizontalSpacer(4.dp)

                    Text(
                        style = LuxTheme.typography.bodyLargeR,
                        fontSize = 14.sp,
                        text = uiState.rssi.toString()
                    )
                }
            }


        }
    }
}