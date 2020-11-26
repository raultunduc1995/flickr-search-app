package com.example.flickrsearchapp

import android.app.Application
import com.example.flickrsearchapp.dagger.AppComponent
import com.example.flickrsearchapp.dagger.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent = DaggerAppComponent.create()
}