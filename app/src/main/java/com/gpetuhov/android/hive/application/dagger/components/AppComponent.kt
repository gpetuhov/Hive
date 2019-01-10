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
import com.gpetuhov.android.hive.ui.epoxy.offer.details.controller.OfferDetailsListController
import com.gpetuhov.android.hive.ui.epoxy.offer.favorite.controller.OfferFavoriteListController
import com.gpetuhov.android.hive.ui.epoxy.offer.update.controller.UpdateOfferListController
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller.PhotoFullscreenListController
import com.gpetuhov.android.hive.ui.epoxy.profile.controller.ProfileListController
import com.gpetuhov.android.hive.ui.epoxy.review.controller.ReviewsListController
import com.gpetuhov.android.hive.ui.epoxy.user.details.controller.UserDetailsListController
import com.gpetuhov.android.hive.ui.epoxy.user.favorite.controller.UserFavoriteListController
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

    fun inject(locationService: LocationService)
    fun inject(messageService: MessageService)

    fun inject(authManager: AuthManager)
    fun inject(locationManager: LocationManager)
    fun inject(mapManager: MapManager)
    fun inject(networkManager: NetworkManager)
    fun inject(notificationManager: NotificationManager)

    fun inject(userViewModel: CurrentUserViewModel)
    fun inject(searchResultViewModel: SearchResultViewModel)
    fun inject(userDetailsViewModel: UserDetailsViewModel)
    fun inject(chatMessagesViewModel: ChatMessagesViewModel)
    fun inject(chatroomsViewModel: ChatroomsViewModel)
    fun inject(unreadMessagesExistViewModel: UnreadMessagesExistViewModel)
    fun inject(favoriteUsersViewModel: FavoriteUsersViewModel)
    fun inject(favoriteOffersViewModel: FavoriteOffersViewModel)
    fun inject(reviewsViewModel: ReviewsViewModel)

    fun inject(profileFragmentPresenter: ProfileFragmentPresenter)
    fun inject(mapFragmentPresenter: MapFragmentPresenter)
    fun inject(userDetailsFragmentPresenter: UserDetailsFragmentPresenter)
    fun inject(chatFragmentPresenter: ChatFragmentPresenter)
    fun inject(chatroomsFragmentPresenter: ChatroomsFragmentPresenter)
    fun inject(updateOfferFragmentPresenter: UpdateOfferFragmentPresenter)
    fun inject(offerDetailsFragmentPresenter: OfferDetailsFragmentPresenter)
    fun inject(locationFragmentPresenter: LocationFragmentPresenter)
    fun inject(favoritesFragmentPresenter: FavoritesFragmentPresenter)
    fun inject(favoriteUsersFragmentPresenter: FavoriteUsersFragmentPresenter)
    fun inject(favoriteOffersFragmentPresenter: FavoriteOffersFragmentPresenter)
    fun inject(updateReviewFragmentPresenter: UpdateReviewFragmentPresenter)
    fun inject(reviewsFragmentPresenter: ReviewsFragmentPresenter)
    fun inject(updateCommentFragmentPresenter: UpdateCommentFragmentPresenter)

    fun inject(deleteUserInteractor: DeleteUserInteractor)
    fun inject(signOutInteractor: SignOutInteractor)
    fun inject(saveUsernameInteractor: SaveUsernameInteractor)
    fun inject(saveDescriptionInteractor: SaveDescriptionInteractor)
    fun inject(searchInteractor: SearchInteractor)
    fun inject(sendMessageInteractor: SendMessageInteractor)
    fun inject(saveOfferInteractor: SaveOfferInteractor)
    fun inject(deleteOfferInteractor: DeleteOfferInteractor)
    fun inject(deleteUserPhotoInteractor: DeleteUserPhotoInteractor)
    fun inject(favoritesInteractor: FavoritesInteractor)
    fun inject(saveReviewInteractor: SaveReviewInteractor)
    fun inject(deleteReviewInteractor: DeleteReviewInteractor)
    fun inject(saveCommentInteractor: SaveCommentInteractor)
    fun inject(deleteCommentInteractor: DeleteCommentInteractor)
    fun inject(savePhoneInteractor: SavePhoneInteractor)
    fun inject(saveEmailInteractor: SaveEmailInteractor)
    fun inject(saveSkypeInteractor: SaveSkypeInteractor)

    fun inject(resultMessagesProvider: ResultMessagesProvider)

    fun inject(profileListController: ProfileListController)
    fun inject(userDetailsListController: UserDetailsListController)
    fun inject(updateOfferListController: UpdateOfferListController)
    fun inject(offerDetailsListController: OfferDetailsListController)
    fun inject(photoFullscreenListController: PhotoFullscreenListController)
    fun inject(offerFavoriteListController: OfferFavoriteListController)
    fun inject(userFavoriteListController: UserFavoriteListController)
    fun inject(reviewsListController: ReviewsListController)
}
