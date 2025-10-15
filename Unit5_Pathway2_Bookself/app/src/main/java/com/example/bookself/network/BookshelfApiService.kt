package com.example.bookself.network

import com.example.bookself.data.BookDetail
import com.example.bookself.data.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {
    // Endpoint để tìm kiếm sách
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookSearchResponse

    // Endpoint để lấy chi tiết một cuốn sách bằng ID
    @GET("volumes/{volumeId}")
    suspend fun getBookDetail(@Path("volumeId") volumeId: String): BookDetail
}