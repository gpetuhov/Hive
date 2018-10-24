package com.gpetuhov.android.hive.application.dagger.components

import com.gpetuhov.android.hive.application.dagger.modules.AppModule
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.managers.NotificationManager
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.service.LocationService
import com.gpetuhov.android.hive.ui.activity.AuthActivity
import com.gpetuhov.android.hive.ui.activity.MainActivity
import com.gpetuhov.android.hive.ui.activity.SplashActivity
import com.gpetuhov.android.hive.ui.fragment.MapFragment
import com.gpetuhov.android.hive.ui.fragment.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(authActivity: AuthActivity)
    fun inject(mainActivity: MainActivity)

    fun inject(mapFragment: MapFragment)
    fun inject(profileFragment: ProfileFragment)

    fun inject(locationService: LocationService)

    fun inject(authManager: AuthManager)
    fun inject(locationManager: LocationManager)
    fun inject(mapManager: MapManager)
    fun inject(notificationManager: NotificationManager)
    fun inject(repository: Repository)

    fun inject(userViewModel: CurrentUserViewModel)
}
