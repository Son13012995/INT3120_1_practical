//package com.example.racetracker.ui
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import kotlinx.coroutines.delay
//import kotlin.random.Random // Thêm import cho Random
//
//class RaceParticipant(
//    val name: String,
//    val maxProgress: Int = 100,
//    val progressDelayMillis: Long = 500L,
//    private val progressIncrement: Int = 10,
//    private val initialProgress: Int = 0
//) {
//    init {
//        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
//        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
//    }
//
//    var isWinner: Boolean by mutableStateOf(false)
//
//    var currentProgress by mutableStateOf(initialProgress)
//        private set
//
//    suspend fun run(onWin: (Boolean) -> Unit) {
//        // sử dụng một giá trị ngẫu nhiên từ 1 đến progressIncrement.
//        val randomIncrementGenerator = Random(System.currentTimeMillis() + name.hashCode()) // Dùng seed khác nhau
//
//        while (currentProgress < maxProgress) {
//            delay(progressDelayMillis)
//
//            // Tăng tiến độ ngẫu nhiên
//            val actualIncrement = randomIncrementGenerator.nextInt(1, progressIncrement + 1)
//            currentProgress += actualIncrement
//
//            // Đảm bảo không vượt quá maxProgress
//            if (currentProgress >= maxProgress) {
//                currentProgress = maxProgress
//                isWinner = true
//                onWin(true)
//                break
//            }
//        }
//    }
//
//    fun reset() {
//        currentProgress = 0;
//        isWinner = false
//    }
//}
//
//
//val RaceParticipant.progressFactor: Float
//    get() = currentProgress / maxProgress.toFloat()
package com.example.racetracker.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    // progressIncrement bây giờ là giá trị TỐI ĐA người chơi có thể chạy trong MỘT NGÀY.
    private val progressIncrement: Int = 10,
    private val initialProgress: Int = 0
) {
    init {
        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
    }

    // Trạng thái người chiến thắng, vẫn giữ nguyên để cập nhật UI
    var isWinner: Boolean by mutableStateOf(false)

    // Tiến độ hiện tại
    var currentProgress by mutableStateOf(initialProgress)
        private set

    // Tạo một generator ngẫu nhiên
    private val randomIncrementGenerator = Random(System.currentTimeMillis() + name.hashCode())

    /**
     * Mô phỏng một ngày trôi qua.
     * Tăng tiến độ ngẫu nhiên dựa trên dailyIncrement (ProgressIncrement).
     */
    fun advanceDay() {

        if (currentProgress < maxProgress) {

            // Lấy một giá trị ngẫu nhiên từ 1 đến progressIncrement
            val actualIncrement = randomIncrementGenerator.nextInt(1, progressIncrement + 1)
            currentProgress += actualIncrement

            if (currentProgress >= maxProgress) {
                currentProgress = maxProgress
                isWinner = true
            }
        }
    }

    /**
     * Đặt lại tiến trình về trạng thái ban đầu.
     */
    fun reset() {
        currentProgress = initialProgress // Đặt về 0
        isWinner = false
    }
}

/**
 * Extension property để tính toán tỷ lệ tiến trình cho thanh ProgressBar.
 */
val RaceParticipant.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()
