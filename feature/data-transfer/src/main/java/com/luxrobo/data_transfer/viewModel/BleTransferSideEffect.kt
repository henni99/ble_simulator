package com.luxrobo.data_transfer.viewModel

sealed class BleTransferSideEffect {

    data object Finish: BleTransferSideEffect()

}