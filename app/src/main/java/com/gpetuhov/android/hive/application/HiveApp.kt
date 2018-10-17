package com.gpetuhov.android.hive.application

import androidx.multidex.MultiDexApplication
import com.gpetuhov.android.hive.BuildConfig
import com.gpetuhov.android.hive.application.dagger.components.AppComponent
import com.gpetuhov.android.hive.application.dagger.components.DaggerAppComponent
import timber.log.Timber

class HiveApp : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var application: HiveApp
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        application = this
        appComponent = DaggerAppComponent.builder().build()
    }
}