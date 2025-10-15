package com.example.flight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flight.FlightSearchApplication
import com.example.flight.data.FlightRepository
import com.example.flight.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // 1. Khai báo StateFlow để ViewModel quản lý trạng thái UI
    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    // 2. Khởi tạo StateFlow tổng hợp
    val searchState: StateFlow<FlightSearchUiState> =
        combine(
            // Flow A: Lấy truy vấn tìm kiếm hiện tại từ DataStore
            userPreferencesRepository.searchQuery,

            // Flow B: Lấy danh sách sân bay yêu thích (được dùng khi query rỗng)
            flightRepository.getAllFavorite()
        ) { latestQuery, favorites ->

            // Logic Lựa chọn Dữ liệu Hiển thị
            val currentContent: SearchContent = if (latestQuery.isBlank()) {
                // TRƯỜNG HỢP 1: Query rỗng -> Hiển thị Danh sách Yêu thích
                SearchContent.FavoritesList(favorites)
            } else {
                // TRƯỜNG HỢP 2: Query KHÔNG rỗng -> BẮT ĐẦU TÌM KIẾM SÂN BAY
                // Chạy hàm tìm kiếm trong luồng riêng và trả về SuggestionList
                val airportSuggestions = flightRepository.getAirportsByQuery(latestQuery)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5000),
                        initialValue = emptyList()
                    )
                    .value // Lấy giá trị ngay lập tức (vì đã có stateIn)

                SearchContent.SuggestionList(airportSuggestions)
            }

            // Cập nhật UiState tổng thể
            FlightSearchUiState(
                searchQuery = latestQuery,
                content = currentContent,
                // Giữ nguyên selectedDepartureAirport
                selectedDepartureAirport = _uiState.value.selectedDepartureAirport
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FlightSearchUiState()
            )


    // 3. Hàm xử lý sự kiện khi người dùng nhập text
    fun onQueryChange(query: String) {
        // Lưu truy vấn tìm kiếm mới vào DataStore
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    // ... (Thêm các hàm khác như selectDepartureAirport, toggleFavorite...)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                val container = application.container // Giả định DI trong Application

                FlightSearchViewModel(
                    flightRepository = container.flightRepository,
                    userPreferencesRepository = container.userPreferencesRepository
                )
            }
        }
    }
}