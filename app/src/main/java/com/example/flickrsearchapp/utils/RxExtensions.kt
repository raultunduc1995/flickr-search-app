package com.example.flickrsearchapp.utils

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Flowable<T>.defaultSchedulers(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.ioSchedulers(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
}