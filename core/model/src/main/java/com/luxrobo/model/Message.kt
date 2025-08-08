package com.luxrobo.model

import com.luxrobo.mapper.currentTime
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Message
 *
 * BLE 기기 또는 시스템에서 생성된 메시지 데이터를 나타내는 데이터 클래스.
 *
 * @property datetime  메시지 생성 시점 (기본값: 현재 시간)
 * @property deviceId  메시지와 연관된 BLE 기기 ID
 * @property name      BLE 기기 이름 또는 메시지 발신자 명칭
 * @property message   메시지 내용 문자열
 */

@Serializable
data class Message(
    val datetime: LocalDateTime = currentTime,
    val deviceId: String,
    val name: String,
    val message: String
)


