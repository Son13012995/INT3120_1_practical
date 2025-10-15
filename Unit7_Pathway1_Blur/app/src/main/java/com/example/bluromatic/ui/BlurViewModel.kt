package com.example.bluromatic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import com.example.bluromatic.BluromaticApplication
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.data.BlurAmountData
import com.example.bluromatic.data.BluromaticRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// ViewModel xử lý logic làm mờ ảnh và trạng thái UI
class BlurViewModel(private val bluromaticRepository: BluromaticRepository) : ViewModel() {

    // Danh sách các mức độ làm mờ
    internal val blurAmount = BlurAmountData.blurAmount

    // Quan sát trạng thái xử lý từ WorkManager
    val blurUiState: StateFlow<BlurUiState> = bluromaticRepository.outputWorkInfo
        .map { info ->
            val outputImageUri = info?.outputData?.getString(KEY_IMAGE_URI)
            when {
                // Hoàn thành và có ảnh đầu ra
                info?.state?.isFinished == true && !outputImageUri.isNullOrEmpty() -> {
                    BlurUiState.Complete(outputUri = outputImageUri)
                }
                // Nếu bị hủy
                info?.state == WorkInfo.State.CANCELLED -> {
                    BlurUiState.Default
                }
                // Đang chạy
                else -> BlurUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BlurUiState.Default
        )

    // Gọi repository để bắt đầu làm mờ ảnh
    fun applyBlur(blurLevel: Int) {
        bluromaticRepository.applyBlur(blurLevel)
    }

    // Hủy tác vụ WorkManager đang chạy
    fun cancelWork() {
        bluromaticRepository.cancelWork()
    }

    // Tạo ViewModel thông qua Factory, inject repository từ Application
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bluromaticRepository =
                    (this[APPLICATION_KEY] as BluromaticApplication).container.bluromaticRepository
                BlurViewModel(bluromaticRepository = bluromaticRepository)
            }
        }
    }
}

// Trạng thái UI: mặc định, đang tải, hoặc hoàn thành
sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: String) : BlurUiState
}
