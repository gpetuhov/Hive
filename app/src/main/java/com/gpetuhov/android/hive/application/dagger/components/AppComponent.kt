package com.gpetuhov.android.hive.application.dagger.components

import com.gpetuhov.android.hive.application.dagger.modules.AppModule
import com.gpetuhov.android.hive.domain.interactor.*
import com.gpetuhov.android.hive.managers.*
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.service.LocationService
import com.gpetuhov.android.hive.ui.activity.AuthActivity
import com.gpetuhov.android.hive.ui.activity.MainActivity
import com.gpetuhov.android.hive.ui.activity.SplashActivity
import com.gpetuhov.android.hive.ui.fragment.MapFragment
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.gpetuhov.android.hive.util.MessagesProvider
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(authActivity: AuthActivity)
    fun inject(mainActivity: MainActivity)

    fun inject(mapFragment: MapFragment)

    fun inject(locationService: LocationService)

    fun inject(authManager: AuthManager)
    fun inject(locationManager: LocationManager)
    fun inject(mapManager: MapManager)
    fun inject(networkManager: NetworkManager)
    fun inject(notificationManager: NotificationManager)

    fun inject(userViewModel: CurrentUserViewModel)
    fun inject(searchResultViewModel: SearchResultViewModel)

    fun inject(profileFragmentPresenter: ProfileFragmentPresenter)

    fun inject(deleteUserInteractor: DeleteUserInteractor)
    fun inject(signOutInteractor: SignOutInteractor)
    fun inject(saveUsernameInteractor: SaveUsernameInteractor)
    fun inject(saveServiceInteractor: SaveServiceInteractor)
    fun inject(deleteServiceInteractor: DeleteServiceInteractor)
    fun inject(saveVisibilityInteractor: SaveVisibilityInteractor)

    fun inject(messagesProvider: MessagesProvider)
}
