package com.gpetuhov.android.hive.application

import android.app.Application
import com.gpetuhov.android.hive.BuildConfig
import timber.log.Timber

class HiveApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}