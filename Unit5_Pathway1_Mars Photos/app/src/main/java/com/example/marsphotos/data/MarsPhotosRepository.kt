import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

interface MarsPhotosRepository {
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

//class NetworkMarsPhotosRepository() : MarsPhotosRepository {
//    override suspend fun getMarsPhotos(): List<MarsPhoto> {
//        return MarsApi.retrofitService.getPhotos()
//    }
//}

class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getMarsPhotos(): List<MarsPhoto> = marsApiService.getPhotos()
}