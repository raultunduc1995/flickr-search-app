package com.example.flickrsearchapp.dagger

import com.example.flickrsearchapp.fragments.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(fragment: MainFragment)
}