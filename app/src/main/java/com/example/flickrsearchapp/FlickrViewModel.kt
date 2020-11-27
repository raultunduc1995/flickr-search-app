package com.example.flickrsearchapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flickrsearchapp.domain.PhotoDetails
import com.example.flickrsearchapp.domain.PresentationPhotoId
import com.example.flickrsearchapp.network.Api
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlickrViewModel @Inject constructor(private val api: Api) {
    private val NUMBER_OF_PHOTOS_PER_PAGE = 30

    private val errorHandler = MutableLiveData<Throwable>()
    val searchPhotosErrorLiveData: LiveData<Throwable>
        get() = errorHandler

    private val searchedPhotos = mutableListOf<PhotoDetails>()
    private val searchedPhotosMutableLiveData = MutableLiveData<List<PhotoDetails>>()
    val photosLiveData: LiveData<List<PhotoDetails>>
        get() = searchedPhotosMutableLiveData

    fun searchPhotosByName(searchText: String): Flowable<MutableList<PhotoDetails>> {
        searchedPhotos.clear()
        return api.searchPhotosByName(searchText)
            .map { it.photos.presentationPhotos.takeLast(NUMBER_OF_PHOTOS_PER_PAGE) }
            .flatMapSingle { photoIds -> getPhotosDetails(photoIds) }
            .doOnError { error -> errorHandler.postValue(error) }
            .doOnComplete { searchedPhotosMutableLiveData.postValue(searchedPhotos) }
    }

    private fun getPhotosDetails(photoIds: List<PresentationPhotoId>): Single<MutableList<PhotoDetails>>? {
        if (photoIds.isEmpty())
            return Single.just(mutableListOf())

        return Flowable.fromIterable(photoIds)
            .flatMap { photoId ->
                api.getPhotoDetails(photoId.id.toString())
                    .map { it.photo }
            }
            .doOnNext {
                searchedPhotos.add(it)
                searchedPhotosMutableLiveData.postValue(searchedPhotos)
            }
            .toList()
    }
}