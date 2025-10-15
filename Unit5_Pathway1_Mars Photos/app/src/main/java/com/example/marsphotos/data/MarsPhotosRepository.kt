package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

interface MarsPhotosRepository {
    // Giữ nguyên tên hàm
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

class NetworkMarsPhotosRepository(
    // Giữ nguyên tên biến
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {

    /** * Fetches list of MarsPhoto from marsApi.
     * ĐÃ SỬA LOGIC: Sử dụng runCatching để bao bọc lệnh gọi,
     * giúp xử lý ngoại lệ mạng một cách tường minh hơn.
     */
    override suspend fun getMarsPhotos(): List<MarsPhoto> {
        return runCatching {

            marsApiService.getPhotos()
        }.getOrElse { exception ->
            // In ra lỗi và trả về danh sách trống nếu có lỗi xảy ra.
            println("Lỗi khi tải ảnh từ Mars API: ${exception.localizedMessage}")
            emptyList()
        }
    }
}