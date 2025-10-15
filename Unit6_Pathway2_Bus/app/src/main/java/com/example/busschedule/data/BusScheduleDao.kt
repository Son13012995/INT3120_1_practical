package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule ORDER BY arrival_time ")
    fun getAll(): Flow<List<Schedule>>


    @Query("SELECT * FROM schedule WHERE stop_name IN (:stopName) ORDER BY arrival_time")
    fun getByStopName(stopName: String): Flow<List<Schedule>>

    @Query("SELECT DISTINCT stop_name FROM schedule")
    fun getAllStopNames(): Flow<List<String>>
}