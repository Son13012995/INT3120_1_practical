package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import com.example.marsphotos.network.MarsPhoto

interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
}

object RetrofitClient {
    private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Khởi tạo Retrofit
    val retrofitInstance: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    // Service được định nghĩa là property
    val apiService: MarsApiService by lazy {
        retrofitInstance.create(MarsApiService::class.java)
    }
}

class DefaultAppContainer : AppContainer {

    /**
     * Cung cấp repository cho toàn ứng dụng.
     * Logic: Lấy API Service từ RetrofitClient và truyền vào Repository.
     */
    override val marsPhotosRepository: MarsPhotosRepository by lazy {
        NetworkMarsPhotosRepository(RetrofitClient.apiService)
    }
}