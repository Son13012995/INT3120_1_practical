package com.example.dessertclicker.data

import androidx.annotation.DrawableRes

data class DessertClickerUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    @DrawableRes val currentDessertImageId: Int = 0, // Sẽ được khởi tạo sau
    val currentDessertPrice: Int = 0 // Sẽ được khởi tạo sau
)