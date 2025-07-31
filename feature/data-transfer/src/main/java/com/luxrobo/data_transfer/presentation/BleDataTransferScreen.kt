package com.luxrobo.data_transfer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luxrobo.ble_simulator.feature.data_transfer.R
import com.luxrobo.data_transfer.ui.MessageInputRow
import com.luxrobo.data_transfer.ui.ScrollableReadOnlyTextField
import com.luxrobo.data_transfer.viewModel.BleDataTransferIntent
import com.luxrobo.data_transfer.viewModel.BleDataTransferUiState
import com.luxrobo.designsystem.component.BasicTopAppBar
import com.luxrobo.designsystem.theme.LuxTheme

@Composable
fun BleDataTransferScreen(
    uiState: BleDataTransferUiState,
    handleIntent: (BleDataTransferIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .systemBarsPadding()
    ) {
        BasicTopAppBar(
            titleRes = R.string.data_transfer_screen,
            actionButtons = {
                Button(
                    onClick = {
                        handleIntent(BleDataTransferIntent.Disconnect)
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                ) {
                    Text(
                        text = stringResource(R.string.connection_closed),
                        style = LuxTheme.typography.bodyLargeR
                    )
                }
            }
        )

        BleDataTransferContent(
            uiState = uiState,
            handleIntent = handleIntent
        )

    }
}

@Composable
fun BleDataTransferContent(
    uiState: BleDataTransferUiState,
    handleIntent: (BleDataTransferIntent) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            ScrollableReadOnlyTextField(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.send_log),
                text = uiState.sendMessages
            )

            Spacer(Modifier.height(16.dp))

            ScrollableReadOnlyTextField(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.receive_log),
                text = uiState.receiveMessages
            )
        }


        MessageInputRow(
            text = text,
            onTextChange = { text = it },
            onSend = { message ->
                handleIntent(BleDataTransferIntent.PostMessage(message))
            }
        )
    }
}