package com.example.bookself.ui
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookself.BookshelfApplication
import com.example.bookself.data.BookshelfRepository
import kotlinx.coroutines.launch
import java.io.IOException
sealed interface BookshelfUiState {
    data class Success(val photos: List<String>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        getBooksPhotos("jazz+history")
    }

    fun getBooksPhotos(query: String) {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            bookshelfUiState = try {
                val photos = bookshelfRepository.getBooksPhotos(query)
                BookshelfUiState.Success(photos)
            } catch (e: IOException) {
                BookshelfUiState.Error
            }
        }
    }

    // Factory để tạo ViewModel với dependency là repository
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Lấy application context để tạo AppContainer
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }
}