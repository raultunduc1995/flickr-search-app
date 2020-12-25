package com.example.flickrsearchapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flickrsearchapp.domain.PhotoDetails
import com.example.flickrsearchapp.domain.PresentationPhotoId
import com.example.flickrsearchapp.network.Api
import com.example.flickrsearchapp.utils.defaultSchedulers
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlickrViewModel @Inject constructor(private val api: Api) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val NUMBER_OF_PHOTOS_PER_PAGE = 30

    private val errorHandler = MutableLiveData<Throwable>()
    val searchPhotosErrorLiveData: LiveData<Throwable>
        get() = errorHandler

    private val searchedPhotosMutableLiveData = MutableLiveData<List<PhotoDetails>>()
    val photosLiveData: LiveData<List<PhotoDetails>>
        get() = searchedPhotosMutableLiveData

    fun searchPhotosByName(searchText: String) {
        compositeDisposable.add(api.searchPhotosByName(searchText)
            .map { it.photos.presentationPhotos.takeLast(NUMBER_OF_PHOTOS_PER_PAGE) }
            .flatMapSingle { photoIds -> getPhotosDetails(photoIds) }
            .defaultSchedulers()
            .subscribe({
                searchedPhotosMutableLiveData.postValue(it)
            }, { error ->
                errorHandler.postValue(error)
            })
        )
    }

    private fun getPhotosDetails(photoIds: List<PresentationPhotoId>): Single<MutableList<PhotoDetails>>? {
        if (photoIds.isEmpty())
            return Single.just(mutableListOf())

        return Flowable.fromIterable(photoIds)
            .flatMap { photoId ->
                api.getPhotoDetails(photoId.id.toString())
                    .map { it.photo }
            }
            .toList()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}