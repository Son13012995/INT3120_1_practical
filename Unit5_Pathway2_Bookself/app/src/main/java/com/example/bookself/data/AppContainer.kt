package com.example.bookself.data


import com.example.bookself.network.BookshelfApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Định nghĩa Base URL dưới dạng private object
 */
private object Config {
    const val BASE_URL = "https://www.googleapis.com/books/v1/"
    val JSON_CONTENT_TYPE = "application/json".toMediaType()
}

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultAppContainer : AppContainer {

    private val BASE_URL = Config.BASE_URL

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Thiết lập timeout rõ ràng
        .build()

    // Tạo đối tượng Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        // ĐÃ THAY ĐỔI: Chuyển sang dùng Kotlinx Serialization Converter
        .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(Config.JSON_CONTENT_TYPE))
        .baseUrl(BASE_URL)
        .client(client) // Truyền client vào
        .build()

    private val retrofitService: BookshelfApiService by lazy {
        run { retrofit.create(BookshelfApiService::class.java) }
    }

    // Tạo repository và truyền service vào
    override val bookshelfRepository: BookshelfRepository by lazy {
        NetworkBookshelfRepository(retrofitService)
    }
}