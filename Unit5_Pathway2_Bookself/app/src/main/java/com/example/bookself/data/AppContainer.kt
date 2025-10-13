import com.example.bookself.data.BookshelfRepository
import com.example.bookself.data.NetworkBookshelfRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://www.googleapis.com/books/v1/"

    // Tạo đối tượng Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    // Tạo service API
    private val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }

    // Tạo repository và truyền service vào
    override val bookshelfRepository: BookshelfRepository by lazy {
        NetworkBookshelfRepository(retrofitService)
    }
}