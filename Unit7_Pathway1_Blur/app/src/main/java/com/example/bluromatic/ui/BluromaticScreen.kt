package com.example.bluromatic.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bluromatic.R
import com.example.bluromatic.data.BlurAmount
import com.example.bluromatic.ui.theme.BluromaticTheme

// Màn hình chính: hiển thị ảnh, mức độ làm mờ, và nút thao tác
@Composable
fun BluromaticScreen(blurViewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)) {
    val uiState by blurViewModel.blurUiState.collectAsStateWithLifecycle()
    val layoutDirection = LocalLayoutDirection.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {
        BluromaticScreenContent(
            blurUiState = uiState,
            blurAmountOptions = blurViewModel.blurAmount,
            applyBlur = blurViewModel::applyBlur,
            cancelWork = blurViewModel::cancelWork,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

// Giao diện hiển thị ảnh, mức độ làm mờ và nút thao tác
@Composable
fun BluromaticScreenContent(
    blurUiState: BlurUiState,
    blurAmountOptions: List<BlurAmount>,
    applyBlur: (Int) -> Unit,
    cancelWork: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedValue by rememberSaveable { mutableStateOf(1) }
    val context = LocalContext.current

    Column(modifier = modifier) {
        // Ảnh hiển thị mẫu
        Image(
            painter = painterResource(R.drawable.android_cupcake),
            contentDescription = stringResource(R.string.description_image),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit
        )

        // Chọn mức độ làm mờ
        BlurAmountContent(
            selectedValue = selectedValue,
            blurAmounts = blurAmountOptions,
            modifier = Modifier.fillMaxWidth(),
            onSelectedValueChange = { selectedValue = it }
        )

        // Nút thao tác (Start / Cancel / See file)
        BlurActions(
            blurUiState = blurUiState,
            onStartClick = { applyBlur(selectedValue) },
            onSeeFileClick = { currentUri -> showBlurredImage(context, currentUri) },
            onCancelClick = { cancelWork() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Các nút thao tác tương ứng với trạng thái UI
@Composable
private fun BlurActions(
    blurUiState: BlurUiState,
    onStartClick: () -> Unit,
    onSeeFileClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        when (blurUiState) {
            is BlurUiState.Default -> {
                Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.start))
                }
            }

            is BlurUiState.Loading -> {
                FilledTonalButton(onClick = onCancelClick) {
                    Text(stringResource(R.string.cancel_work))
                }
                CircularProgressIndicator(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }

            is BlurUiState.Complete -> {
                Button(onClick = onStartClick) { Text(stringResource(R.string.start)) }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                FilledTonalButton({ onSeeFileClick(blurUiState.outputUri) }) {
                    Text(stringResource(R.string.see_file))
                }
            }
        }
    }
}

// Giao diện chọn mức độ làm mờ (radio buttons)
@Composable
private fun BlurAmountContent(
    selectedValue: Int,
    blurAmounts: List<BlurAmount>,
    modifier: Modifier = Modifier,
    onSelectedValueChange: (Int) -> Unit
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = stringResource(R.string.blur_title),
            style = MaterialTheme.typography.headlineSmall
        )

        blurAmounts.forEach { amount ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (selectedValue == amount.blurAmount),
                        onClick = { onSelectedValueChange(amount.blurAmount) }
                    )
                    .size(48.dp)
            ) {
                RadioButton(
                    selected = (selectedValue == amount.blurAmount),
                    onClick = null,
                    modifier = Modifier.size(48.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(stringResource(amount.blurAmountRes))
            }
        }
    }
}

// Mở ảnh đã làm mờ
private fun showBlurredImage(context: Context, currentUri: String) {
    val uri = if (currentUri.isNotEmpty()) Uri.parse(currentUri) else null
    val actionView = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(actionView)
}

// Preview trong Android Studio
@Preview(showBackground = true)
@Composable
fun BluromaticScreenContentPreview() {
    BluromaticTheme {
        BluromaticScreenContent(
            blurUiState = BlurUiState.Default,
            blurAmountOptions = listOf(BlurAmount(R.string.blur_lv_1, 1)),
            applyBlur = {},
            cancelWork = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
