package com.example.flight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flight.FlightSearchApplication
import com.example.flight.data.Airport
import com.example.flight.data.Favorite
import com.example.flight.data.FlightRepository
import com.example.flight.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// ----------------------------
// UI State đại diện toàn màn hình
// ----------------------------
data class FlightUiState(
    val searchQuery: String = "",
    val selectedAirport: Airport? = null,
    val suggestions: List<Airport> = emptyList(),
    val flightList: List<Airport> = emptyList(),
    val favoriteList: List<Favorite> = emptyList()
)

// ----------------------------
// ViewModel chính của ứng dụng
// ----------------------------
class FlightViewModel(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightUiState())
    val uiState: StateFlow<FlightUiState> = _uiState

    init {
        // 1️⃣ Lấy lại query đã lưu và load suggestion ban đầu
        viewModelScope.launch {
            userPreferencesRepository.searchQuery.collectLatest { savedQuery ->
                _uiState.value = _uiState.value.copy(searchQuery = savedQuery)
                updateSuggestions(savedQuery)
            }
        }

        // 2️⃣ Lấy danh sách favorite từ database
        viewModelScope.launch {
            flightRepository.getAllFavorites().collectLatest { favorites ->
                _uiState.value = _uiState.value.copy(favoriteList = favorites)
            }
        }
    }

    // ----------------------------
    // Khi người dùng nhập query
    // ----------------------------
    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(newQuery)
            updateSuggestions(newQuery)
        }
    }

    // ----------------------------
    // Cập nhật gợi ý sân bay theo query
    // ----------------------------
    private fun updateSuggestions(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                flightRepository.getAirportsByQuery(query).collectLatest { result ->
                    _uiState.value = _uiState.value.copy(suggestions = result)
                }
            } else {
                _uiState.value = _uiState.value.copy(suggestions = emptyList())
            }
        }
    }

    // ----------------------------
    // Khi chọn 1 sân bay từ gợi ý
    // ----------------------------
    fun onAirportSelected(airport: Airport) {
        _uiState.value = _uiState.value.copy(selectedAirport = airport)
        viewModelScope.launch {
            flightRepository.getAllPossibleDestinations(airport.iataCode).collectLatest { destinations ->
                _uiState.value = _uiState.value.copy(flightList = destinations)
            }
        }
    }

    // ----------------------------
    // Thêm / Xóa route yêu thích
    // ----------------------------
    fun onFavoriteClick(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val currentList = _uiState.value.favoriteList
            val existing = currentList.find {
                it.departureCode == departureCode && it.destinationCode == destinationCode
            }

            if (existing != null) {
                // Nếu đã có → xóa
                flightRepository.deleteFavorite(existing)
            } else {
                // Nếu chưa có → thêm mới
                flightRepository.insertFavorite(
                    Favorite(departureCode = departureCode, destinationCode = destinationCode)
                )
            }
        }
    }

    // ----------------------------
    // Factory khởi tạo ViewModel
    // ----------------------------
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // 🔥 Sửa chỗ container lỗi
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                val flightRepository = application.container.flightRepository
                val userPreferencesRepository = application.container.userPreferencesRepository
                FlightViewModel(flightRepository, userPreferencesRepository)
            }
        }
    }
}
