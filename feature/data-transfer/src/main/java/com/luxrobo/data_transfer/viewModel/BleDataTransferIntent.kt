package com.luxrobo.data_transfer.viewModel

sealed interface BleDataTransferIntent {

    data object Disconnect : BleDataTransferIntent

    data class PostMessage(val msg: String) : BleDataTransferIntent


}