package com.luxrobo.data_transfer.ext

import android.os.Parcelable
import com.luxrobo.model.BleDeviceInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableBleDeviceInfo(
    val deviceId: String,
    val name: String,
    val rssi: Long
): Parcelable

fun BleDeviceInfo.toParcelize() = ParcelableBleDeviceInfo(
    deviceId = deviceId,
    name = name,
    rssi = rssi
)

fun ParcelableBleDeviceInfo.toOrigin() = BleDeviceInfo(
    deviceId = deviceId,
    name = name,
    rssi = rssi
)
