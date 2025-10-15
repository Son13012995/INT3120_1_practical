package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flight.data.Airport
import com.example.flight.data.Favorite
import com.example.flight.ui.FlightViewModel

@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    viewModel: FlightViewModel = viewModel(factory = FlightViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Thanh tìm kiếm
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onQueryChange(it) },
                label = { Text("Search for an airport") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Hiển thị nội dung
            if (uiState.searchQuery.isBlank()) {
                FavoriteList(
                    favorites = uiState.favoriteList,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                SuggestionList(
                    suggestions = uiState.suggestions,
                    onSuggestionClick = { airport -> viewModel.onAirportSelected(airport) },
                    flightList = uiState.flightList,
                    favoriteList = uiState.favoriteList,
                    onFavoriteClick = viewModel::onFavoriteClick, // Sẽ không còn lỗi ở đây
                    departureAirport = uiState.selectedAirport
                )
            }
        }
    }
}

@Composable
fun SuggestionList(
    suggestions: List<Airport>,
    onSuggestionClick: (Airport) -> Unit,
    flightList: List<Airport>,
    favoriteList: List<Favorite>,
    onFavoriteClick: (String, String) -> Unit,
    departureAirport: Airport?,
    modifier: Modifier = Modifier
) {
    // Nếu chưa chọn sân bay nào, hiển thị danh sách gợi ý
    if (departureAirport == null) {
        LazyColumn(modifier = modifier) {
            items(suggestions) { airport ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(airport) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = airport.name)
                }
            }
        }
    } else {
        // Nếu đã chọn sân bay, hiển thị danh sách kết quả
        FlightResultList(
            departure = departureAirport,
            destinations = flightList,
            favoriteList = favoriteList,
            onFavoriteClick = onFavoriteClick
        )
    }
}

@Composable
fun FlightResultList(
    departure: Airport,
    destinations: List<Airport>,
    favoriteList: List<Favorite>,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(destinations) { destination ->
            val isFavorite = favoriteList.any {
                it.departureCode == departure.iataCode && it.destinationCode == destination.iataCode
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "DEPART")
                        Text(text = "${departure.iataCode} ${departure.name}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "ARRIVE")
                        Text(text = "${destination.iataCode} ${destination.name}", fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = { onFavoriteClick(departure.iataCode, destination.iataCode) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFFFC107) else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteList(
    favorites: List<Favorite>,
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "You have no favorite routes.")
        }
    } else {
        Column(modifier = modifier) {
            Text(text = "Favorite Routes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(favorites) { favorite ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = "Depart: ${favorite.departureCode} - Arrive: ${favorite.destinationCode}")
                        }
                    }
                }
            }
        }
    }
}