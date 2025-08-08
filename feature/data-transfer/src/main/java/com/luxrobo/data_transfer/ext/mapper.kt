package com.luxrobo.data_transfer.ext

import android.os.Parcelable
import com.luxrobo.model.BleDeviceInfo
import kotlinx.parcelize.Parcelize

/**
 * BLE 기기 정보를 안드로이드 컴포넌트 간 안전하게 전달하기 위해 Parcelable 형태로 변환한 데이터 클래스와
 * 원본 데이터 클래스 간의 매핑 확장 함수들을 정의한 파일.
 *
 * - ParcelableBleDeviceInfo : 안드로이드에서 Intent, Bundle 등을 통해 전달 가능하도록 만든 BLE 기기 정보 데이터 클래스
 * - BleDeviceInfo.toParcelize() : 원본 BleDeviceInfo → Parcelable 형태로 변환
 * - ParcelableBleDeviceInfo.toOrigin() : Parcelable 형태 → 원본 BleDeviceInfo로 변환
 *
 * 사용 예시:
 * val parcelableInfo = bleDeviceInfo.toParcelize()
 * val originalInfo = parcelableInfo.toOrigin()
 */

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
