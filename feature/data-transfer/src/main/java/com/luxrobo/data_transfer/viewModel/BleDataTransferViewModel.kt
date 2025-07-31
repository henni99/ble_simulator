package com.luxrobo.data_transfer.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luxrobo.data_transfer.ext.ParcelableBleDeviceInfo
import com.luxrobo.data_transfer.ext.toOrigin
import com.luxrobo.domain.usecase.GetMessageUseCase
import com.luxrobo.domain.usecase.PostMessageUseCase
import com.luxrobo.model.BleDeviceInfo
import com.luxrobo.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.toString

@HiltViewModel
class BleDataTransferViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessageUseCase: GetMessageUseCase,
    private val postMessageUseCase: PostMessageUseCase
) : ViewModel() {

    private val _sideEffect = Channel<BleTransferSideEffect>()
    val sideEffect get() = _sideEffect.receiveAsFlow()

    private val _deviceInfo = savedStateHandle.getStateFlow<ParcelableBleDeviceInfo?>("deviceInfo", null)

    private val _sendMessages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())

    private val _receiveMessages: StateFlow<List<Message>> =
        _deviceInfo.filterNotNull().flatMapLatest {
            getMessageUseCase(it.toOrigin()).map { receiveMessage ->
                _receiveMessages.value + listOf(receiveMessage)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    val uiState: StateFlow<BleDataTransferUiState> =
        combine(
            _deviceInfo,
            _sendMessages,
            _receiveMessages
        ) { deviceInfo, sendMessages, receiveMessages ->

            if (deviceInfo == null) {
                BleDataTransferUiState.empty()
            } else {
                BleDataTransferUiState(
                    deviceInfo = deviceInfo.toOrigin(),
                    sendMessages = sendMessages.joinToString(separator = "\n") { it.message },
                    receiveMessages = receiveMessages.joinToString(separator = "\n") { it.message }
                )
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BleDataTransferUiState.empty(),
        )

    fun postMessage(msg: String) = viewModelScope.launch {
        postMessageUseCase(
            Message(
                deviceId = _deviceInfo.value?.deviceId ?: "",
                name = _deviceInfo.value?.name ?: "",
                message = msg
            )
        ).collect { message ->
            _sendMessages.update {
                _sendMessages.value + listOf(message)
            }
        }
    }

    fun disconnect() {
        postSideEffect(BleTransferSideEffect.Finish)
    }

    fun handleIntent(intent: BleDataTransferIntent) {
        when (intent) {
            is BleDataTransferIntent.Disconnect -> {
                disconnect()
            }

            is BleDataTransferIntent.PostMessage -> {
                postMessage(intent.msg)
            }
        }
    }

    fun postSideEffect(effect: BleTransferSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}