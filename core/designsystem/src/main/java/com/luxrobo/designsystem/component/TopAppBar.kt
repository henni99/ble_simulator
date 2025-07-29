package com.luxrobo.designsystem.component

import android.R
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luxrobo.designsystem.theme.LuxTheme

@Composable
fun BasicTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceDim,
    actionButtons: @Composable () -> Unit = {},
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(containerColor)
                .then(modifier)
        ) {

            actionButtons()


            Text(
                text = stringResource(id = titleRes),
                style = LuxTheme.typography.titleLargeB,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun BasicTopAppBarPreview() {
    BasicTopAppBar(
        titleRes = R.string.untitled,
    )
}
