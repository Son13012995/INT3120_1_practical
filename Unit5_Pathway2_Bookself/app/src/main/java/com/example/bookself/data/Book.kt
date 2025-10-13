package com.example.bookself.data

// Dùng để hứng kết quả từ lần gọi API đầu tiên (tìm kiếm)
data class BookSearchResponse(
    val items: List<BookItem>
)

data class BookItem(
    val id: String
)

// Dùng để hứng kết quả từ lần gọi API thứ hai (chi tiết sách)
data class VolumeInfo(
    val imageLinks: ImageLinks?
)

data class BookDetail(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class ImageLinks(
    val thumbnail: String?
)