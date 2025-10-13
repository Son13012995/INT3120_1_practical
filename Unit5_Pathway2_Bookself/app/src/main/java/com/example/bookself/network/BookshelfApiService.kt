import com.example.bookself.data.BookDetail
import com.example.bookself.data.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {
    // Endpoint để tìm kiếm sách
    // Ví dụ: .../volumes?q=jazz+history
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookSearchResponse

    // Endpoint để lấy chi tiết một cuốn sách bằng ID
    // Ví dụ: .../volumes/EpUTEAAAQBAJ
    @GET("volumes/{volumeId}")
    suspend fun getBookDetail(@Path("volumeId") volumeId: String): BookDetail
}