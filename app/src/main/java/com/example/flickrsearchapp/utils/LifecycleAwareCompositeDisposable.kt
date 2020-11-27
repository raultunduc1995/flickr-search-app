package com.example.flickrsearchapp.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class LifecycleAwareCompositeDisposable : LifecycleObserver {
    private var lifecycle: Lifecycle? = null
    private val compositeDisposable = CompositeDisposable()

    fun attachLifecycle(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
    }

    fun subscribe(disposable: Disposable) {
        if (lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == true)
            compositeDisposable.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clearDisposables() {
        compositeDisposable.clear()
    }
}