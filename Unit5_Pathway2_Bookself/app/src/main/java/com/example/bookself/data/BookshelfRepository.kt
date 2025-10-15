package com.example.bookself.data

import com.example.bookself.network.BookshelfApiService

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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

        // Thực hiện lệnh gọi API chi tiết sách song song
        return coroutineScope {
            // Tạo một danh sách cho mỗi lệnh gọi API
            val deferredDetails = bookIds.map { bookId ->
                async {
                    bookshelfApiService.getBookDetail(bookId)
                }
            }

            // Chờ tất cả các lệnh gọi hoàn thành (awaitAll)
            deferredDetails.mapNotNull { deferred ->
                // Trích xuất URL ảnh
                deferred.await().volumeInfo.imageLinks?.thumbnail?.replace("http", "https")
            }
        }
    }
}
