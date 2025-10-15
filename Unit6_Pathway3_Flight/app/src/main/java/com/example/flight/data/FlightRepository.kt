package com.example.flight.data

import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getAirportsByQuery(query: String): Flow<List<Airport>>
    fun getAirportByCode(iataCode: String): Flow<Airport>
    fun getAllPossibleDestinations(iataCode: String): Flow<List<Airport>>
    fun getAllFavorites(): Flow<List<Favorite>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
}
