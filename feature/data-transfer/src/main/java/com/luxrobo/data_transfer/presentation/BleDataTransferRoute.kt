package com.luxrobo.data_transfer.presentation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.luxrobo.data_transfer.viewModel.BleDataTransferViewModel
import com.luxrobo.data_transfer.viewModel.BleTransferSideEffect

@Composable
fun BleDataTransferRoute(
    viewModel: BleDataTransferViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {

                    is BleTransferSideEffect.Finish -> {
                        (context as Activity).finish()
                    }
                }
            }
        }
    }

    BleDataTransferScreen(
        uiState = uiState,
        handleIntent = viewModel::handleIntent
    )

}