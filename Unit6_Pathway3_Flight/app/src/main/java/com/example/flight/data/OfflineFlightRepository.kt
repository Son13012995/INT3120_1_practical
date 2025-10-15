// File: OfflineFlightRepository.kt (Cải tiến)

package com.example.flight.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // Thêm import mới

class OfflineFlightRepository(private val flightDao: FlightDao) : FlightRepository {
    // --- AirportRepository Implementation ---
    override fun getAirportsByQuery(query: String): Flow<List<Airport>> =
        flightDao.getAirportsByQuery(query)

    override fun getAirportByCode(iataCode: String): Flow<Airport> =
        flightDao.getAirportByCode(iataCode)

    override fun getAllPossibleDestinations(iataCode: String): Flow<List<Airport>> =
        flightDao.getAllPossibleDestinations(iataCode)

    // --- FavoriteRepository Implementation ---
    override fun getAllFavorite(): Flow<List<Favorite>> =

        flightDao.getAllFavorite()

    override suspend fun insertFavorite(favorite: Favorite) =
        flightDao.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) =
        flightDao.deleteFavorite(favorite)
}