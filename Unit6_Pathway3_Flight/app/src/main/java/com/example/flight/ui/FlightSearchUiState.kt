package com.example.flight.ui

import com.example.flight.data.Airport
import com.example.flight.data.Favorite

// Định nghĩa lớp Sealed Class cho nội dung chính của màn hình
sealed interface SearchContent {
    // 1. Trạng thái khi hiển thị danh sách yêu thích
    data class FavoritesList(val favorites: List<Favorite>) : SearchContent

    // 2. Trạng thái khi hiển thị gợi ý tìm kiếm
    data class SuggestionList(val suggestions: List<Airport>) : SearchContent

    // 3. Trạng thái ban đầu (hoặc sau khi chọn một sân bay)
    data object Empty : SearchContent
}

// Lớp UiState tổng thể của ViewModel
data class FlightSearchUiState(
    // Trạng thái tìm kiếm từ DataStore
    val searchQuery: String = "",

    // Nội dung chính hiển thị trên màn hình
    val content: SearchContent = SearchContent.Empty,

    // Sân bay đã chọn (ví dụ: cho việc hiển thị điểm đến)
    val selectedDepartureAirport: Airport? = null
)