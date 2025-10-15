package com.example.bluromatic.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.work.*
import com.example.bluromatic.IMAGE_MANIPULATION_WORK_NAME
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.TAG_OUTPUT
import com.example.bluromatic.getImageUri
import com.example.bluromatic.workers.BlurWorker
import com.example.bluromatic.workers.CleanupWorker
import com.example.bluromatic.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    // URI ảnh gốc
    private var imageUri: Uri = context.getImageUri()
    private val workManager = WorkManager.getInstance(context)

    // ID của worker lưu ảnh cuối cùng
    private var outputWorkId: UUID? = null

    // Theo dõi tiến trình worker theo tag
    override val outputWorkInfo: Flow<WorkInfo?> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
            .asFlow()
            .map { list -> list.firstOrNull() }


    override fun applyBlur(blurLevel: Int) {
        // Bước 1: Dọn file tạm
        var continuation = workManager.beginWith(OneTimeWorkRequest.from(CleanupWorker::class.java))

        // 🟢 Thêm quy tắc ràng buộc pin không yếu
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        // Bước 2: Làm mờ ảnh
        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))

        // 🟢 Gắn constraint vào blur worker
        blurBuilder.setConstraints(constraints)

        continuation = continuation.then(blurBuilder.build())

        // Bước 3: Lưu ảnh
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        // Bắt đầu chuỗi công việc
        continuation.enqueue()

    }

    // Hủy toàn bộ công việc đang chạy
    override fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    // Tạo input data chứa mức độ làm mờ và URI ảnh
    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        return Data.Builder()
            .putString(KEY_IMAGE_URI, imageUri.toString())
            .putInt(KEY_BLUR_LEVEL, blurLevel)
            .build()
    }
}
