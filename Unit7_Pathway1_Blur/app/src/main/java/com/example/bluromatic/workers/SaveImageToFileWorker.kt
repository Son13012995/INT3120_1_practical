package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import com.example.bluromatic.domain.CreateImageUriUseCase
import com.example.bluromatic.domain.SaveImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "SaveImageToFileWorker"

// Worker lưu ảnh mờ ra file vĩnh viễn (MediaStore)
class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override suspend fun doWork(): Result {
        // Hiển thị thông báo khi bắt đầu lưu ảnh
        makeStatusNotification(
            applicationContext.resources.getString(R.string.saving_image),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            // Giả lập độ trễ để dễ quan sát tiến trình
            delay(DELAY_TIME_MILLIS)

            val resolver = applicationContext.contentResolver

            return@withContext try {
                // Lấy URI ảnh mờ được truyền từ BlurWorker
                val resourceUri = inputData.getString(KEY_IMAGE_URI)
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                val imageUrl: String?

                // Android 10+ dùng MediaStore API mới
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val createImageUriUseCase = CreateImageUriUseCase()
                    val saveImageUseCase = SaveImageUseCase()

                    // Tạo URI lưu ảnh trong thư mục Pictures/Blur-O-Matic
                    val imageUri = createImageUriUseCase(
                        resolver,
                        "${title}_${dateFormatter.format(Date())}.jpg"
                    )

                    // Lưu bitmap vào URI vừa tạo
                    saveImageUseCase(resolver, imageUri, bitmap)
                    imageUrl = imageUri.toString()

                } else {
                    // Dành cho thiết bị cũ (dưới Android 10)
                    @Suppress("DEPRECATION")
                    imageUrl = MediaStore.Images.Media.insertImage(
                        resolver, bitmap, title, dateFormatter.format(Date())
                    )
                }

                // Nếu lưu thành công → trả về URI kết quả
                if (!imageUrl.isNullOrEmpty()) {
                    val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                    Result.success(output)
                } else {
                    Log.e(TAG, applicationContext.getString(R.string.writing_to_mediaStore_failed))
                    Result.failure()
                }

            } catch (exception: Exception) {
                Log.e(TAG, applicationContext.getString(R.string.error_saving_image), exception)
                Result.failure()
            }
        }
    }
}
