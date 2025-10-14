package com.example.busschedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.busschedule.BusScheduleApplication
import com.example.busschedule.data.Schedule
import com.example.busschedule.data.ScheduleDao
import kotlinx.coroutines.flow.Flow

class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {
    // Lấy danh sách tất cả các lịch trình từ DAO
    fun getFullSchedule(): Flow<List<Schedule>> {
        return scheduleDao.getAll()
    }

    // Lấy danh sách lịch trình cho một trạm cụ thể từ DAO
    fun getScheduleFor(stopName: String): Flow<List<Schedule>> {
        return scheduleDao.getByStopName(stopName)
    }

    /**
     * Factory để tạo ViewModel, tự động lấy DAO từ Application.
     * Cách làm mới và được khuyến nghị.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Lấy application context và truy cập database.dao
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BusScheduleApplication)
                BusScheduleViewModel(application.database.scheduleDao())
            }
        }
    }
}