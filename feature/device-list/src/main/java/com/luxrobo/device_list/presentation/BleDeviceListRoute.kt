package com.luxrobo.device_list.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.luxrobo.data_transfer.presentation.BleDataTransferActivity
import com.luxrobo.data_transfer.ext.toParcelize
import com.luxrobo.device_list.viewModel.BleDeviceListSideEffect
import com.luxrobo.device_list.viewModel.BleDeviceListViewModel

@Composable
fun BleDeviceListRoute(
    viewModel: BleDeviceListViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val currentState = remember { mutableStateOf(lifecycle.currentState) }

    DisposableEffect(currentState) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                // onPause 시점 처리
                viewModel.resetSelection()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {

                    is BleDeviceListSideEffect.MoveToDetail -> {

                        val intent = Intent(context, BleDataTransferActivity::class.java)
                        intent.putExtra("deviceInfo", sideEffect.deviceInfo.toParcelize())
                        context.startActivity(intent)
                    }
                }
            }

        }
    }


    BleDeviceListScreen(
        uiState = uiState,
        handleIntent = viewModel::handleIntent
    )


}