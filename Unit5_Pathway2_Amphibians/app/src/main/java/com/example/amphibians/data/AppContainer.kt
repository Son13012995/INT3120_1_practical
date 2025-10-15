package com.example.amphibians.data
import com.example.amphibians.network.AmphibianApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Interface cho dependency container.
 */
interface AppContainer {
    val amphibianRepository: AmphibianRepository
}
// Custom JSON Decoder
private val customJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}


class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com/"

    private val apiClient: Retrofit = Retrofit.Builder()
        // Dùng customJson đã định nghĩa
        .addConverterFactory(customJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .validateEagerly(true)
        .build()

    /**
     * Đối tượng Retrofit service được tạo bởi Retrofit.
     * hàm lambda.
     */
    private val retrofitService: AmphibianApiService by lazy {
        { apiClient.create(AmphibianApiService::class.java) }.invoke()
    }
    
    override val amphibianRepository: AmphibianRepository by lazy {
        NetworkAmphibianRepository(amphibianApiService = retrofitService)
    }
}