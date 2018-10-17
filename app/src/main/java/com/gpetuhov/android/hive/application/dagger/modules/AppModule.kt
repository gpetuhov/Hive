package com.gpetuhov.android.hive.application.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providesContext(): Context {
        return HiveApp.application.applicationContext
    }

    @Provides
    @Singleton
    fun providesPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun providesLocationManager(): LocationManager {
        return LocationManager()
    }
}
