package com.luxrobo.mapper

/**
 * getConnectionQuality
 *
 * RSSI 값을 받아 연결 품질 상태 문자열 리소스 ID를 문자열로 변환하여 반환 (현재는 단순 toString 호출).
 *
 * @param rssi RSSI 신호 세기 값
 * @return 연결 품질 상태 문자열 (리소스 ID의 문자열 표현)
 */

 fun getConnectionQuality(rssi: Long): String {
    return when {
        rssi > -60 -> connection_quality_excellent
        rssi in -70 until -60 -> connection_quality_good
        rssi in -80 until -70 -> connection_quality_fair
        rssi <= -80 -> connection_quality_poor
        else -> connection_quality_unknown
    }.toString()
}

const val connection_quality_excellent = "매우 안정적인 연결이 가능해요"
const val connection_quality_good = "안정적인 연결이 가능해요"
const val connection_quality_fair = "연결 가능하지만 불안정할 수 있어요.\n끊김 가능성 있습니다"
const val connection_quality_poor = "매우 불안정 합니다.\n연결 실패 또는 자주 끊김 문제가 생길 수 있습니다"
const val connection_quality_unknown = "알 수 없음"
