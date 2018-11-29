package com.gpetuhov.android.hive.application.dagger.components

import com.gpetuhov.android.hive.application.dagger.modules.AppModule
import com.gpetuhov.android.hive.domain.interactor.*
import com.gpetuhov.android.hive.managers.*
import com.gpetuhov.android.hive.presentation.presenter.*
import com.gpetuhov.android.hive.service.LocationService
import com.gpetuhov.android.hive.service.MessageService
import com.gpetuhov.android.hive.ui.activity.AuthActivity
import com.gpetuhov.android.hive.ui.activity.MainActivity
import com.gpetuhov.android.hive.ui.activity.SplashActivity
import com.gpetuhov.android.hive.ui.epoxy.profile.controller.ProfileListController
import com.gpetuhov.android.hive.ui.fragment.MapFragment
import com.gpetuhov.android.hive.ui.viewmodel.*
import com.gpetuhov.android.hive.util.ResultMessagesProvider
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
    fun inject(messageService: MessageService)

    fun inject(authManager: AuthManager)
    fun inject(locationManager: LocationManager)
    fun inject(mapManager: MapManager)
    fun inject(networkManager: NetworkManager)
    fun inject(notificationManager: NotificationManager)

    fun inject(userViewModel: CurrentUserViewModel)
    fun inject(searchResultViewModel: SearchResultViewModel)
    fun inject(detailsViewModel: DetailsViewModel)
    fun inject(chatMessagesViewModel: ChatMessagesViewModel)
    fun inject(chatroomsViewModel: ChatroomsViewModel)
    fun inject(unreadMessagesExistViewModel: UnreadMessagesExistViewModel)

    fun inject(profileFragmentPresenter: ProfileFragmentPresenter)
    fun inject(mapFragmentPresenter: MapFragmentPresenter)
    fun inject(userDetailsFragmentPresenter: UserDetailsFragmentPresenter)
    fun inject(chatFragmentPresenter: ChatFragmentPresenter)
    fun inject(chatroomsFragmentPresenter: ChatroomsFragmentPresenter)

    fun inject(deleteUserInteractor: DeleteUserInteractor)
    fun inject(signOutInteractor: SignOutInteractor)
    fun inject(saveUsernameInteractor: SaveUsernameInteractor)
    fun inject(saveDescriptionInteractor: SaveDescriptionInteractor)
    fun inject(saveOnlineInteractor: SaveOnlineInteractor)
    fun inject(searchInteractor: SearchInteractor)
    fun inject(sendMessageInteractor: SendMessageInteractor)

    fun inject(resultMessagesProvider: ResultMessagesProvider)

    fun inject(profileListController: ProfileListController)
}
