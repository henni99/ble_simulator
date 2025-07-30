package com.luxrobo.device_list.viewModel

import com.luxrobo.model.BleDeviceInfo

sealed class BleDeviceListSideEffect {
    data class MoveToDetail(
        val deviceInfo: BleDeviceInfo
    ) : BleDeviceListSideEffect()

}