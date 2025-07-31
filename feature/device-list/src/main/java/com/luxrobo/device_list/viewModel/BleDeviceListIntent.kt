package com.luxrobo.device_list.viewModel

import com.luxrobo.model.BleDeviceConnection

sealed interface BleDeviceListIntent {

    data object ChangeScanState : BleDeviceListIntent

    data object ResetSelection : BleDeviceListIntent

    data class SelectDevice(
        val deviceConnection: BleDeviceConnection
    ) : BleDeviceListIntent

    data object DismissDialog : BleDeviceListIntent
}