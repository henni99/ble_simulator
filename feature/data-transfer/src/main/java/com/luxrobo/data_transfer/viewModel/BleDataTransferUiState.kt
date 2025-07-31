package com.luxrobo.data_transfer.viewModel


import androidx.compose.runtime.Immutable
import com.luxrobo.model.BleDeviceInfo

@Immutable
data class BleDataTransferUiState(
    val deviceInfo: BleDeviceInfo?,
    val sendMessages: String = "",
    val receiveMessages: String = ""
) {
    companion object {
        fun empty() = BleDataTransferUiState(
            deviceInfo = null,
            sendMessages = "",
            receiveMessages = ""
        )
    }
}
