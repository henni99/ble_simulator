package com.luxrobo.device_list.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luxrobo.ble_simulator.feature.device_list.R
import com.luxrobo.designsystem.component.BasicTopAppBar
import com.luxrobo.designsystem.theme.LuxTheme
import com.luxrobo.device_list.ui.BasicDialog
import com.luxrobo.device_list.ui.BleDeviceConnection
import com.luxrobo.device_list.ui.ScanningRippleEffect
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.device_list.viewModel.BleDeviceListUiState

@Composable
fun BleDeviceListScreen(
    uiState: BleDeviceListUiState,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .systemBarsPadding()
    ) {
        BasicTopAppBar(
            titleRes = R.string.bluetooth_list_screen,
            actionButtons = {
                Button(
                    onClick = {
                        handleIntent(BleDeviceListIntent.ChangeScanState)
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                ) {
                    Text(if (uiState.isScanning) "스캔 종료" else "스캔 시작")
                }
            }
        )
        BleDeviceListContent(
            uiState = uiState,
            handleIntent = handleIntent
        )
    }
}

@Composable
fun BleDeviceListContent(
    uiState: BleDeviceListUiState,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.bluetooth_list_scanning_title),
                    style = LuxTheme.typography.titleLargeB
                )

                Spacer(modifier = Modifier.weight(1f)) // ← 공간 확보용 Spacer

                if (uiState.isScanning) {
                    ScanningRippleEffect(
                        modifier = Modifier
                            .testTag("스캔 애니메이션")
                            .size(48.dp) // 또는 적당한 크기 지정
                    )
                }
            }


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = uiState.bleDeviceConnections,
                    key = { it.deviceId }
                ) { bleDeviceConnection ->
                    BleDeviceConnection(
                        uiState = bleDeviceConnection,
                        handleIntent = handleIntent
                    )
                }
            }
        }

        if (uiState.isConnecting) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .testTag("로딩"),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.isDialogShowed.first) {
            BasicDialog(
                isDialogTitle = uiState.isDialogShowed.second,
                onDismissRequest = handleIntent
            )
        }
    }
}


@Preview
@Composable
private fun ContributorScreenPreview() {
    BleDeviceListScreen(
        uiState = BleDeviceListUiState.empty(),
        handleIntent = {},
    )
}