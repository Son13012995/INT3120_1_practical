package com.example.flight.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightRepository(private val flightDao: FlightDao) : FlightRepository {
    override fun getAirportsByQuery(query: String): Flow<List<Airport>> =
        flightDao.getAirportsByQuery(query)

    override fun getAirportByCode(iataCode: String): Flow<Airport> =
        flightDao.getAirportByCode(iataCode)

    override fun getAllPossibleDestinations(iataCode: String): Flow<List<Airport>> =
        flightDao.getAllPossibleDestinations(iataCode)

    override fun getAllFavorites(): Flow<List<Favorite>> =
        flightDao.getAllFavorites()

    override suspend fun insertFavorite(favorite: Favorite) =
        flightDao.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) =
        flightDao.deleteFavorite(favorite)
}
