package com.example.marsphotos.network

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * A public interface that exposes the [getPhotos] method
 */
interface MarsApiService {
    /**
     * Returns a [List] of [MarsPhoto] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>

//    @GET
//    suspend fun getPhotosFromUrl(@Url url: String = "photos"): List<MarsPhoto>
//
}