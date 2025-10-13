package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import kotlinx.coroutines.flow.update

// UI state của game
data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Lưu từ hiện tại
    private lateinit var currentWord: String

    // Set lưu các từ đã dùng
    private var usedWords: MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

    /** Reset lại game */
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    /** Chọn từ random và xáo chữ */
    private fun pickRandomWordAndShuffle(): String {
        if (usedWords.size == allWords.size) {
            usedWords.clear() // hoặc xử lý gameOver
        }
        val unused = allWords.filterNot { usedWords.contains(it) }
        currentWord = unused.random()
        usedWords.add(currentWord)
        return shuffleCurrentWord(currentWord)
    }
   //Xáo từ
    private fun shuffleCurrentWord(word: String): String {
        if (word.length <= 1) return word
        val chars = word.toCharArray()
        do {
            chars.shuffle()
        } while (String(chars) == word)
        return String(chars)
    }
    //Cập nhật lại user guesss
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    //Kiểm tra guese của user
    fun checkUserGuess() {
        if (_uiState.value.isGameOver) return
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // Người dùng trả lời đúng , +1 điểm
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            // Người dùng đoán sai, hiện lỗi
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset lại guess của người chơi
        updateUserGuess("")
    }
    //Skip word
    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }

    //Update điểm ới cho user
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS){
            //Last round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }
}
