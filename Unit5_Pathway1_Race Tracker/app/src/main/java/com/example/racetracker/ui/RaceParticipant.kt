package com.example.racetracker.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

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
        while (currentProgress < maxProgress) {
            delay(progressDelayMillis)
            currentProgress += progressIncrement
            if (currentProgress == maxProgress) {
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
