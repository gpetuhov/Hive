package com.gpetuhov.android.hive.application

import androidx.multidex.MultiDexApplication
import com.gpetuhov.android.hive.application.dagger.components.AppComponent
import com.gpetuhov.android.hive.application.dagger.components.DaggerAppComponent
import com.gpetuhov.android.hive.util.CrashlyticsTree
import timber.log.Timber

// Class is open so that it can be mocked in tests by Mockito
open class HiveApp : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var application: HiveApp
    }

    override fun onCreate() {
        super.onCreate()

        // Write logs to Crashlytics both in debug and release
        Timber.plant(CrashlyticsTree())

        application = this
        appComponent = DaggerAppComponent.builder().build()
    }
}