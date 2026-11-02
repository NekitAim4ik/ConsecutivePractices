package com.practicum.practice3

import android.app.Application
import com.practicum.practice3.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Practice3App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Practice3App)
            modules(appModule)
        }
    }
}