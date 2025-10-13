package com.example.bookself.data

import BookshelfApiService

interface BookshelfRepository {
    suspend fun getBooksPhotos(query: String): List<String>
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {

    override suspend fun getBooksPhotos(query: String): List<String> {
        // Gọi API tìm kiếm để lấy danh sách các ID
        val searchResponse = bookshelfApiService.searchBooks(query)

        // Dùng map để lặp qua từng `BookItem` và lấy `id`
        val bookIds = searchResponse.items.map { it.id }

        // Dùng map để lặp qua từng ID, gọi API lấy chi tiết sách và trích xuất URL ảnh bìa
        return bookIds.mapNotNull { bookId ->
            val bookDetail = bookshelfApiService.getBookDetail(bookId)
            // Lấy URL thumbnail và thay thế http bằng https
            bookDetail.volumeInfo.imageLinks?.thumbnail?.replace("http", "https")
        }
    }
}