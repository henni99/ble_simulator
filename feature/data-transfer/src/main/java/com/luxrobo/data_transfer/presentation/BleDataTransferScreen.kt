package com.luxrobo.data_transfer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luxrobo.ble_simulator.feature.data_transfer.R
import com.luxrobo.data_transfer.ui.MessageInputRow
import com.luxrobo.data_transfer.ui.ScrollableReadOnlyTextField
import com.luxrobo.data_transfer.viewModel.BleDataTransferIntent
import com.luxrobo.data_transfer.viewModel.BleDataTransferUiState
import com.luxrobo.designsystem.component.BasicTopAppBar
import com.luxrobo.designsystem.component.VerticalSpacer
import com.luxrobo.designsystem.theme.LuxTheme

@Composable
fun BleDataTransferScreen(
    uiState: BleDataTransferUiState,
    handleIntent: (BleDataTransferIntent) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            BasicTopAppBar(
                titleRes = R.string.data_transfer_screen,
                logoButtons = {

                    IconButton(
                        onClick = {
                            handleIntent(BleDataTransferIntent.Disconnect)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.connection_closed)
                        )
                    }
                }
            )
        },
        bottomBar = {
            MessageInputRow(
                modifier = Modifier,
                text = text,
                onTextChange = { text = it },
                onSend = { message ->
                    handleIntent(BleDataTransferIntent.PostMessage(message))
                }
            )
        }
    ) { paddingValues ->
        BleDataTransferContent(
            modifier = Modifier
                .padding(paddingValues),
            uiState = uiState
        )
    }
}

@Composable
fun BleDataTransferContent(
    modifier: Modifier = Modifier,
    uiState: BleDataTransferUiState,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 16.dp
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                text = "디바이스 정보",
                fontSize = 16.sp,
                style = LuxTheme.typography.titleLargeB
            )

            VerticalSpacer(8.dp)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                color = Color.DarkGray,
                text = uiState.deviceInfo?.name.toString(),
                style = LuxTheme.typography.bodyLargeR
            )

            VerticalSpacer(8.dp)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = uiState.deviceInfo?.deviceId.toString(),
                color = Color.DarkGray,
                style = LuxTheme.typography.bodyLargeR
            )
        }

        VerticalSpacer(8.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .height(4.dp)
        )


        Column(
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 16.dp
                )
        ) {
            VerticalSpacer(8.dp)

            Column(
                modifier = Modifier
            ) {
                ScrollableReadOnlyTextField(
                    modifier = Modifier
                        .weight(1f),
                    title = stringResource(R.string.send_log),
                    text = uiState.sendMessages
                )

                    VerticalSpacer(16.dp)

                    ScrollableReadOnlyTextField(
                        modifier = Modifier
                            .weight(1f),
                        title = stringResource(R.string.receive_log),
                        text = uiState.receiveMessages
                    )
            }
        }
    }


}
