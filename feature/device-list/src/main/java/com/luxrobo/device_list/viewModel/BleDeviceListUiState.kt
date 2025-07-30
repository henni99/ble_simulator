package com.luxrobo.device_list.viewModel

import androidx.compose.runtime.Immutable
import com.luxrobo.model.BleDeviceConnection

@Immutable
data class BleDeviceListUiState(
    val isScanning: Boolean = true,
    val isDialogShowed: Pair<Boolean, String>,
    val bleDeviceConnections: List<BleDeviceConnection>,
    val isConnecting: Boolean = false
) {
    companion object {
        fun empty() = BleDeviceListUiState(
            isScanning = true,
            isDialogShowed = Pair(false, ""),
            bleDeviceConnections = emptyList(),
            isConnecting = false
        )
    }
}