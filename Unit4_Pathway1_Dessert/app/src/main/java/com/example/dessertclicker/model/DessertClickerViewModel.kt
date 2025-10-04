package com.example.dessertclicker.model

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.data.DessertClickerUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertClickerViewModel : ViewModel() {

    // Danh sách món tráng miệng tĩnh
    private val allDesserts = Datasource.dessertList

    // Biến trạng thái nội bộ có thể thay đổi (MutableStateFlow)
    private val _uiState = MutableStateFlow(DessertClickerUiState())

    // Biến trạng thái công khai chỉ đọc (StateFlow)
    val uiState: StateFlow<DessertClickerUiState> = _uiState.asStateFlow()

    // Theo dõi món tráng miệng hiện tại (để dễ dàng truy cập giá)
    private var currentDessert = allDesserts.first()
    private var currentDessertIndex = 0 // Để theo dõi vị trí trong danh sách

    init {
        // Khởi tạo trạng thái ban đầu với món tráng miệng đầu tiên
        _uiState.update { currentState ->
            currentState.copy(
                currentDessertImageId = currentDessert.imageId,
                currentDessertPrice = currentDessert.price
            )
        }
    }

    /**
     * Logic được gọi khi người dùng click vào món tráng miệng.
     */
    fun onDessertClicked() {
        // 1. Cập nhật Doanh thu và Số lượng bán
        val newRevenue = _uiState.value.revenue + currentDessert.price
        val newDessertsSold = _uiState.value.dessertsSold + 1

        // 2. Xác định món tráng miệng tiếp theo (nếu có)
        val nextDessertIndex = determineDessertIndex(newDessertsSold)

        // 3. Cập nhật trạng thái UI
        if (nextDessertIndex != currentDessertIndex) {
            // Nâng cấp lên món tráng miệng mới
            currentDessertIndex = nextDessertIndex
            currentDessert = allDesserts[currentDessertIndex]
            _uiState.update { currentState ->
                currentState.copy(
                    revenue = newRevenue,
                    dessertsSold = newDessertsSold,
                    currentDessertImageId = currentDessert.imageId,
                    currentDessertPrice = currentDessert.price
                )
            }
        } else {
            // Vẫn là món tráng miệng cũ
            _uiState.update { currentState ->
                currentState.copy(
                    revenue = newRevenue,
                    dessertsSold = newDessertsSold,
                )
            }
        }
    }

    /**
     * Xác định chỉ mục (index) của món tráng miệng cần hiển thị.
     */
    private fun determineDessertIndex(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (i in allDesserts.indices) {
            if (dessertsSold >= allDesserts[i].startProductionAmount) {
                dessertIndex = i
            } else {
                break
            }
        }
        return dessertIndex
    }
}