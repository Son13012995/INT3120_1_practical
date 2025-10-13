//Tạo một "Container" để quản lý và cung cấp các dependency
// (như Retrofit, Repository) cho toàn bộ ứng dụng.
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

//Lớp triển khai container, tạo và quản lý các dependency.
class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com/"

     //Dùng Retrofit builder để tạo đối tượng Retrofit.
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Đối tượng Retrofit service được tạo bởi Retrofit.
     * Dùng lazy để chỉ được khởi tạo khi được gọi lần đầu.
     */
    private val retrofitService: AmphibianApiService by lazy {
        retrofit.create(AmphibianApiService::class.java)
    }


    //Cung cấp repository cho toàn ứng dụng.
    override val amphibianRepository: AmphibianRepository by lazy {
        NetworkAmphibianRepository(retrofitService)
    }
}