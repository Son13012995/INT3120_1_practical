package com.example.amphibians.data

import com.example.amphibians.model.Amphibian
import com.example.amphibians.network.AmphibianApiService

//Inteface định nghĩa các lấy dữ liệu, giúp dễ fake data
interface AmphibianRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

//Lớp triển khai lấy dữ liệu thực tế từ API
class NetworkAmphibianRepository(
    private val amphibianApiService: AmphibianApiService
) : AmphibianRepository {
     //Lấy danh sách lưỡng cư từ ApiService.
    override suspend fun getAmphibians(): List<Amphibian> = amphibianApiService.getAmphibians()
}