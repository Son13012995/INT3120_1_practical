package com.example.flight.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    // Lấy danh sách sân bay cho việc tự động hoàn thành
    @Query("""
        SELECT * FROM airport 
        WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%' 
        ORDER BY passengers DESC
    """)
    fun getAirportsByQuery(query: String): Flow<List<Airport>>

    // Lấy một sân bay cụ thể bằng mã IATA
    @Query("SELECT * FROM airport WHERE iata_code = :iataCode")
    fun getAirportByCode(iataCode: String): Flow<Airport>

    // Lấy tất cả các sân bay khác để làm điểm đến
    @Query("SELECT * FROM airport WHERE iata_code != :iataCode ORDER BY name ASC")
    fun getAllPossibleDestinations(iataCode: String): Flow<List<Airport>>

    // Lấy tất cả các chuyến bay yêu thích
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    // Thêm một chuyến bay vào danh sách yêu thích
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    // Xóa một chuyến bay khỏi danh sách yêu thích
    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}