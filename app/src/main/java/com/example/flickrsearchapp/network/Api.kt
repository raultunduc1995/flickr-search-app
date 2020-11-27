package com.example.flickrsearchapp.network

import com.example.flickrsearchapp.BuildConfig
import com.example.flickrsearchapp.domain.PhotoDetailsResponse
import com.example.flickrsearchapp.domain.PhotoResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/services/rest/?method=flickr.photos.search&api_key=${BuildConfig.API_KEY}&format=json&nojsoncallback=1")
    fun searchPhotosByName(@Query("text") searchText: String): Flowable<PhotoResponse>

    @GET("/services/rest/?method=flickr.photos.getInfo&api_key=${BuildConfig.API_KEY}&format=json&nojsoncallback=1")
    fun getPhotoDetails(@Query("photo_id") photoId: String): Flowable<PhotoDetailsResponse>
}