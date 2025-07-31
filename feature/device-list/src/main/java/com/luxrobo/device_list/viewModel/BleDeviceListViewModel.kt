package com.luxrobo.device_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.mapper.toBleDeviceInfo
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleDeviceListViewModel @Inject constructor(
    private val getBleDeviceConnectionsUseCase: GetBleDeviceConnectionsUseCase
): ViewModel() {

    private val _sideEffect = Channel<BleDeviceListSideEffect>()
    val sideEffect get() = _sideEffect.receiveAsFlow()

    private val _selectedDeviceId = MutableStateFlow<String?>(null)

    private val _isScanning = MutableStateFlow<Boolean>(true)

    private val _isDialogShowed = MutableStateFlow<Pair<Boolean, String>>(Pair(false, ""))

    fun controlledBleDeviceFlow(): Flow<List<BleDeviceConnection>> {
        return _isScanning
            .flatMapLatest { isEnabled ->
                if (isEnabled) {
                    getBleDeviceConnectionsUseCase()
                } else {
                    flowOf(emptyList())
                }
            }
    }

    val uiState: StateFlow<BleDeviceListUiState> =
        combine(
            controlledBleDeviceFlow(),
            _selectedDeviceId,
            _isScanning,
            _isDialogShowed
        ) { deviceConnections, selectedDeviceId, isScanning, isDialogShowed ->
            val updated = deviceConnections.map {
                it.copy(isConnecting = it.deviceId == selectedDeviceId)
            }

            BleDeviceListUiState(
                bleDeviceConnections = updated,
                isConnecting = selectedDeviceId != null,
                isScanning = isScanning,
                isDialogShowed = isDialogShowed
            )
        }.catch { throwable ->
            _isDialogShowed.update { Pair(true, "문제가 발생했습니다") }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BleDeviceListUiState.empty(),
        )

    fun selectDevice(deviceConnection: BleDeviceConnection) = viewModelScope.launch {

        if (deviceConnection.rssi >= -70) {
            _selectedDeviceId.update { deviceConnection.deviceId }
            delay(1000L)
            postSideEffect(BleDeviceListSideEffect.MoveToDetail(deviceConnection.toBleDeviceInfo()))
        } else {
            _isDialogShowed.update { Pair(true, getConnectionQuality(deviceConnection.rssi)) }
        }
    }

    fun dismissDialog() = viewModelScope.launch {
        _isDialogShowed.update { Pair(false, "") }
    }

    fun resetSelection() = viewModelScope.launch {
        _selectedDeviceId.update { null }
    }

    fun changeScanState() = viewModelScope.launch {
        _isScanning.update { !_isScanning.value }
    }

    fun postSideEffect(effect: BleDeviceListSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }

    fun handleIntent(intent: BleDeviceListIntent) {
        when (intent) {
            is BleDeviceListIntent.ChangeScanState -> {
                changeScanState()
            }

            is BleDeviceListIntent.ResetSelection -> {
                resetSelection()
            }

            is BleDeviceListIntent.SelectDevice -> {
                selectDevice(intent.deviceConnection)
            }

            is BleDeviceListIntent.DismissDialog -> {
                dismissDialog()
            }
        }
    }
}

fun getConnectionQuality(rssi: Long): String {
    return when {
        rssi > -60 -> "매우 안정적인 연결"
        rssi in -70 until -60 -> "안정적인 연결"
        rssi in -80 until -70 -> "연결 가능하지만 불안정, 끊김 가능성 있음"
        rssi <= -80 -> "매우 불안정, 연결 실패 또는 자주 끊김"
        else -> "알 수 없음"
    }
}