package com.gpetuhov.android.hive.ui.epoxy.profile.controller

import android.content.Context
import com.gpetuhov.android.hive.BuildConfig
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.profile.models.*
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class ProfileListController(private val presenter: ProfileFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var signOutEnabled = true
    private var deleteAccountEnabled = true

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        val photoList = user?.photoList ?: mutableListOf()

        addPhoto {
            id("addPhoto")
            onClick { presenter.choosePhoto() }
            maxPhotoWarningVisible(photoList.size >= Constants.User.MAX_VISIBLE_PHOTO_COUNT)
        }

        photoCarousel(
            settings,
            photoList,
            false,
            true,
            { photoUrlList -> presenter.openPhotos(photoUrlList) },
            { photoUid -> presenter.showDeletePhotoDialog(photoUid) }
        )

        profileUsername {
            id("profile_username")

            val hasUsername = user?.hasUsername ?: false
            username(if (hasUsername) user?.username ?: "" else context.getString(R.string.enter_username))
            onUsernameClick { presenter.showUsernameDialog() }

            userPicUrl(user?.userPicUrl ?: "")
            onUserPicClick { presenter.chooseUserPic() }

            name(user?.name ?: "")
            email(user?.email ?: "")
        }

        summary(context, true) { presenter.openAllReviews() }

        profileContacts {
            id("profile_contacts")

            val hasPhone = user?.hasPhone ?: false
            phone(if (hasPhone) user?.phone ?: "" else context.getString(R.string.enter_phone))
            onPhoneClick { presenter.showPhoneDialog() }

            val hasVisibleEmail = user?.hasVisibleEmail ?: false
            email(if (hasVisibleEmail) user?.visibleEmail ?: "" else context.getString(R.string.enter_email))
            onEmailClick { presenter.showEmailDialog() }

            onUseRegistrationEmailClick { presenter.saveRegistrationEmailAsVisibleEmail() }

            val hasSkype = user?.hasSkype ?: false
            skype(if (hasSkype) user?.skype ?: "" else context.getString(R.string.enter_skype))
            onSkypeClick { presenter.showSkypeDialog() }

            val hasFacebook = user?.hasFacebook ?: false
            facebook(if (hasFacebook) user?.facebook ?: "" else context.getString(R.string.enter_facebook))
            onFacebookClick { presenter.showFacebookDialog() }
        }

        profileAbout {
            id("profile_about")

            val hasDescription = user?.hasDescription ?: false
            description(if (hasDescription) user?.description ?: "" else context.getString(R.string.enter_description))
            onDescriptionClick { presenter.showDescriptionDialog() }
        }

        profileOffersHeader {
            id("profile_offers_header")

            val hasActiveOffer = user?.hasActiveOffer() ?: false
            noActiveOffersWarningVisible(!hasActiveOffer)
        }

        user?.offerList?.forEach { offer ->
            userOfferItem(
                context,
                settings,
                offer,
                true,
                false,
                { /* Do nothing */ },
                { presenter.updateOffer(offer.uid) }
            )
        }

        addOffer {
            id("addOffer")
            onClick { presenter.updateOffer("") }
        }

        reviewsHeader()

        reviewsSummary(context, true) { presenter.openAllReviews() }

        settings {
            id("settings")

            onSignOutClick { presenter.showSignOutDialog() }
            signOutEnabled(signOutEnabled)

            onDeleteAccountClick { presenter.showDeleteUserDialog() }
            deleteAccountEnabled(deleteAccountEnabled)

            onPrivacyPolicyClick { presenter.openPrivacyPolicy() }

            appVersion("${context.getString(R.string.app_version)}: ${BuildConfig.VERSION_NAME}")
        }
    }

    fun signOutEnabled(isEnabled: Boolean) {
        signOutEnabled = isEnabled
        requestModelBuild()
    }

    fun deleteAccountEnabled(isEnabled: Boolean) {
        deleteAccountEnabled = isEnabled
        requestModelBuild()
    }
}