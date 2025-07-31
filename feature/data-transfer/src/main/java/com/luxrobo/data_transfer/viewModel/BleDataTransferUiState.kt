package com.luxrobo.data_transfer.viewModel


import androidx.compose.runtime.Immutable
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BleDataTransferUiState(
    val deviceInfo: BleDeviceInfo?,
    val sendMessages: ImmutableList<Message> = persistentListOf(),
    val receiveMessages: ImmutableList<Message> = persistentListOf()
) {
    companion object {
        fun empty() = BleDataTransferUiState(
            deviceInfo = null,
            sendMessages = persistentListOf(),
            receiveMessages = persistentListOf()
        )
    }
}
