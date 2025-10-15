package com.example.racetracker.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.random.Random // Thêm import cho Random

class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMillis: Long = 500L,
    private val progressIncrement: Int = 10,
    private val initialProgress: Int = 0
) {
    init {
        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
    }

    var isWinner: Boolean by mutableStateOf(false)

    var currentProgress by mutableStateOf(initialProgress)
        private set

    suspend fun run(onWin: (Boolean) -> Unit) {
        // sử dụng một giá trị ngẫu nhiên từ 1 đến progressIncrement.
        val randomIncrementGenerator = Random(System.currentTimeMillis() + name.hashCode()) // Dùng seed khác nhau

        while (currentProgress < maxProgress) {
            delay(progressDelayMillis)

            // Tăng tiến độ ngẫu nhiên
            val actualIncrement = randomIncrementGenerator.nextInt(1, progressIncrement + 1)
            currentProgress += actualIncrement

            // Đảm bảo không vượt quá maxProgress
            if (currentProgress >= maxProgress) {
                currentProgress = maxProgress
                isWinner = true
                onWin(true)
                break
            }
        }
    }

    fun reset() {
        currentProgress = 0;
        isWinner = false
    }
}


val RaceParticipant.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()