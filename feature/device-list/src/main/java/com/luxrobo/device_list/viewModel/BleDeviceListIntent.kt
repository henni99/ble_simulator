package com.luxrobo.device_list.viewModel

import com.luxrobo.model.BleDeviceInfo

sealed interface BleDeviceListIntent {

    data object ChangeScanState: BleDeviceListIntent

    data object ResetSelection : BleDeviceListIntent

    data class SelectDevice(
        val deviceInfo: BleDeviceInfo
    ) : BleDeviceListIntent

    data object DismissDialog: BleDeviceListIntent
}