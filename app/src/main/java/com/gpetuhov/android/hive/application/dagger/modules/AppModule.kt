package com.gpetuhov.android.hive.application.dagger.modules

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.managers.*
import com.gpetuhov.android.hive.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providesContext(): Context = HiveApp.application.applicationContext

    @Provides
    @Singleton
    fun providesLocationManager(context: Context) = LocationManager(context)

    @Provides
    @Singleton
    fun providesMapManager() = MapManager()

    @Provides
    @Singleton
    fun providesRepository() = Repository()

    @Provides
    @Singleton
    fun providesAuthManager() = AuthManager()

    @Provides
    @Singleton
    fun providesNotificationManager() = NotificationManager()

    @Provides
    @Singleton
    fun providesAuth(authManager: AuthManager): Auth = authManager

    @Provides
    @Singleton
    fun providesNetwork(): Network = NetworkManager()
}
