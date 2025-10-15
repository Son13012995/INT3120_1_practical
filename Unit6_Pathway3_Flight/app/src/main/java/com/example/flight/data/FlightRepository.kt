package com.example.flight.data

import kotlinx.coroutines.flow.Flow

// 1. Interface cho dữ liệu Sân bay/Chuyến bay
interface AirportRepository {
    fun getAirportsByQuery(query: String): Flow<List<Airport>>
    fun getAirportByCode(iataCode: String): Flow<Airport>
    fun getAllPossibleDestinations(iataCode: String): Flow<List<Airport>>
}

// 2. Interface cho dữ liệu Yêu thích
interface FavoriteRepository {
    fun getAllFavorite(): Flow<List<Favorite>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
}

// 3. Khai báo interface chung (để sử dụng trong AppContainer)
interface FlightRepository : AirportRepository, FavoriteRepository // FlightRepository kế thừa cả hai