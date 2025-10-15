package com.example.amphibians.data

import com.example.amphibians.model.Amphibian
import com.example.amphibians.network.AmphibianApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Inteface to mock data
interface AmphibianRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

class NetworkAmphibianRepository(
    private val amphibianApiService: AmphibianApiService
) : AmphibianRepository {
    //Lấy danh sách lưỡng cư từ ApiService.
    override suspend fun getAmphibians(): List<Amphibian> = withContext(Dispatchers.IO) {
        val fetchedList = amphibianApiService.getAmphibians()

        // chỉ quan tâm đến lưỡng cư có tên không rỗng.
        return@withContext fetchedList.filter { it.name.isNotEmpty() }
    }
}