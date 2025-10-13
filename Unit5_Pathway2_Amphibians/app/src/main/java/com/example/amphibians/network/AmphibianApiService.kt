package com.example.amphibians.network

import com.example.amphibians.model.Amphibian
import retrofit2.http.GET

//Interface định nghĩa cách Retrofit giao tiếp với web server thông qua các HTTP request.
interface AmphibianApiService {
    //Lay danh sách loài lưỡng cư từ endpoint amphibians, dùng suspend để dùng couroutine gọi
    @GET("amphibians")
    suspend fun getAmphibians(): List<Amphibian>
}