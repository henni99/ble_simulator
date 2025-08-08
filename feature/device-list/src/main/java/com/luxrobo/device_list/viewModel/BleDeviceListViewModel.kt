package com.luxrobo.device_list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luxrobo.domain.usecase.GetBleDeviceConnectionsUseCase
import com.luxrobo.mapper.getConnectionQuality
import com.luxrobo.mapper.toBleDeviceInfo
import com.luxrobo.model.BleDeviceConnection
import com.luxrobo.model.BleDeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    getBleDeviceConnectionsUseCase: GetBleDeviceConnectionsUseCase
): ViewModel() {

    private val _sideEffect = Channel<BleDeviceListSideEffect>()
    val sideEffect get() = _sideEffect.receiveAsFlow()

    private val _selectedDeviceId = MutableStateFlow<String?>(null)

    private val _isScanning = MutableStateFlow<Boolean>(true)

    private val _isDialogShowed = MutableStateFlow<Pair<Boolean, String>>(Pair(false, ""))

    val uiState: StateFlow<BleDeviceListUiState> =
        combine(
            getBleDeviceConnectionsUseCase(),
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
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BleDeviceListUiState.empty(),
        )

    fun selectDevice(deviceConnection: BleDeviceConnection) = viewModelScope.launch(Dispatchers.IO) {

        if (deviceConnection.rssi >= CONNECTION_PIVOT_VALUE) {
            _selectedDeviceId.update { deviceConnection.deviceId }
            delay(1000L) // 의도적 연결 시간 1초
            postSideEffect(BleDeviceListSideEffect.MoveToDetail(deviceConnection.toBleDeviceInfo()))
        } else {
            _isDialogShowed.update { Pair(true, getConnectionQuality(deviceConnection.rssi)) }
        }
    }

    fun dismissDialog() = viewModelScope.launch(Dispatchers.IO) {
        _isDialogShowed.update { Pair(false, "") }
    }

    fun resetSelection() = viewModelScope.launch(Dispatchers.IO) {
        _selectedDeviceId.update { null }
    }

    fun changeScanState() = viewModelScope.launch(Dispatchers.IO) {
        _isScanning.update { !_isScanning.value }
    }

    fun postSideEffect(effect: BleDeviceListSideEffect) {
        viewModelScope.launch(Dispatchers.IO) {
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

    companion object {
        const val CONNECTION_PIVOT_VALUE = -75
    }
}