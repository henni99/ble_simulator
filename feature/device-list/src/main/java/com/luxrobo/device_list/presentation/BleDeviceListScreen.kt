package com.luxrobo.device_list.presentation


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luxrobo.ble_simulator.feature.device_list.R
import com.luxrobo.designsystem.component.BasicTopAppBar
import com.luxrobo.designsystem.component.SpringyButton
import com.luxrobo.designsystem.theme.LuxColor.Blue01
import com.luxrobo.designsystem.theme.LuxTheme
import com.luxrobo.device_list.ui.BasicDialog
import com.luxrobo.device_list.ui.BleDeviceConnection
import com.luxrobo.device_list.ui.ScanningRippleEffect
import com.luxrobo.device_list.viewModel.BleDeviceListIntent
import com.luxrobo.device_list.viewModel.BleDeviceListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleDeviceListScreen(
    uiState: BleDeviceListUiState,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // 완전히 열거나 닫기만
    )


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
        ,
        containerColor = Color.White,
        topBar = {
            BasicTopAppBar(
                titleRes = R.string.bluetooth_list_screen
            )
        },
        bottomBar = {
            SpringyButton(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                text = "스캔 시작",
                onClick = {
                    println("uiState: ${uiState.isScanning}")
                    handleIntent(BleDeviceListIntent.ChangeScanState)
                }
            )
        }
    ) { paddingValues ->
        BleDeviceListContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            handleIntent = handleIntent
        )



        if (uiState.isScanning) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                dragHandle = null,
                onDismissRequest = {  handleIntent(BleDeviceListIntent.ChangeScanState) },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.White
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.TopCenter),
                        text = stringResource(R.string.bluetooth_list_scanning_title),
                        style = LuxTheme.typography.titleLargeB
                    )

                    ScanningRippleEffect(
                        modifier = Modifier
                            .testTag("스캔 애니메이션")
                            .size(180.dp) // 또는 적당한 크기 지정
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun BleDeviceListContent(
    modifier: Modifier = Modifier,
    uiState: BleDeviceListUiState,
    handleIntent: (BleDeviceListIntent) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        contentPadding = PaddingValues(
            top = 4.dp,
            bottom = 4.dp
        ),
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

    if (uiState.isConnecting) {
        Box(
            modifier = Modifier.fillMaxSize()
                .testTag("로딩"),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Blue01
            )
        }
    }

    if (uiState.isDialogShowed.first) {
        BasicDialog(
            isDialogTitle = uiState.isDialogShowed.second,
            onDismissRequest = handleIntent
        )
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