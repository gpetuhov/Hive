package com.gpetuhov.android.hive.application.dagger.modules

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.managers.*
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.ResultMessagesProvider
import com.gpetuhov.android.hive.util.Settings
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
    fun providesLocationMapManager() = LocationMapManager()

    @Provides
    @Singleton
    fun providesNotificationManager() = NotificationManager()

    @Provides
    @Singleton
    fun providesAuth(): Auth = AuthManager()

    @Provides
    @Singleton
    fun providesNetwork(): Network = NetworkManager()

    @Provides
    @Singleton
    fun providesResultMessages(): ResultMessages = ResultMessagesProvider()

    @Provides
    @Singleton
    fun providesSettings(context: Context) = Settings(context)

    @Provides
    @Singleton
    fun providesRepo(context: Context, settings: Settings): Repo = Repository(context, settings)

    @Provides
    @Singleton
    fun providesOnlineStatusManager(repo: Repo) = OnlineStatusManager(repo)

    @Provides
    @Singleton
    fun providesActivityRecognitionManager(context: Context) = ActivityRecognitionManager(context)
}
