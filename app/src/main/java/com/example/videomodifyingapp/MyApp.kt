package com.example.videomodifyingapp

import android.app.Application
import com.example.videomodifyingapp.di.dataModule
import com.example.videomodifyingapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}