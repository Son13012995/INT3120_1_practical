package com.example.bookself.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BookSearchResponse(
    val items: List<BookItem>
)

@Serializable
data class BookItem(
    val id: String
)

// Dùng để hứng kết quả từ lần gọi API thứ hai (chi tiết sách)
@Serializable
data class VolumeInfo(
    val imageLinks: ImageLinks?
)

@Serializable
data class BookDetail(
    val id: String,
    val volumeInfo: VolumeInfo
)

@Serializable
data class ImageLinks(
      @SerialName("thumbnail")
    val thumbnail: String?
)