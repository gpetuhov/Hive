package com.gpetuhov.android.hive.application.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.repository.Repository
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
    fun providesLocationManager(context: Context): LocationManager {
        return LocationManager(context)
    }

    @Provides
    @Singleton
    fun providesMapManager(): MapManager {
        return MapManager()
    }

    @Provides
    @Singleton
    fun providesRepository(): Repository {
        return Repository()
    }
}
