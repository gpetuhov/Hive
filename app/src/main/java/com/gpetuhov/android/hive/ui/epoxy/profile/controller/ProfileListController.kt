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

            val userPicUrl = user?.userPicUrl ?: ""
            val userPicBigUrl = user?.userPicBigUrl ?: ""

            userPicUrl(userPicUrl)

            onUserPicClick {
                if (userPicUrl != "") {
                    val resultUrl = if (userPicBigUrl != "") userPicBigUrl else userPicUrl
                    presenter.openUserPic(resultUrl)
                }
            }

            onUserPicChangeClick { presenter.chooseUserPic() }

            onUserPicDeleteClick { if (userPicUrl != "") presenter.showDeleteUserPicDialog() }

            name(user?.name ?: "")
            email(user?.email ?: "")
        }

        val hasStatus = user?.hasStatus ?: false
        val hasActivity = user?.hasActivity ?: false
        val status = if (hasStatus) user?.status ?: "" else context.getString(R.string.enter_status)

        status(
            status,
            true,
            hasActivity,
            true,
            { presenter.showStatusDialog() },
            { userActivityType -> presenter.openUserActivity(userActivityType) }
        )

        summary(context, true) { presenter.openAllReviews() }

        awards(true) { awardType -> presenter.openAward(awardType) }

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

            val hasTwitter = user?.hasTwitter ?: false
            twitter(if (hasTwitter) user?.twitter ?: "" else context.getString(R.string.enter_twitter))
            onTwitterClick { presenter.showTwitterDialog() }

            val hasInstagram = user?.hasInstagram ?: false
            instagram(if (hasInstagram) user?.instagram ?: "" else context.getString(R.string.enter_instagram))
            onInstagramClick { presenter.showInstagramDialog() }

            val hasYouTube= user?.hasYouTube ?: false
            youTube(if (hasYouTube) user?.youTube ?: "" else context.getString(R.string.enter_youtube))
            onYouTubeClick { presenter.showYouTubeDialog() }

            val hasWebsite= user?.hasWebsite ?: false
            website(if (hasWebsite) user?.website ?: "" else context.getString(R.string.enter_website))
            onWebsiteClick { presenter.showWebsiteDialog() }
        }

        profileAbout {
            id("profile_about")

            val hasDescription = user?.hasDescription ?: false
            description(if (hasDescription) user?.description ?: "" else context.getString(R.string.enter_description))
            onDescriptionClick { presenter.showDescriptionDialog() }
        }

        profileInformation {
            id("profile_information")

            val hasResidence = user?.hasResidence ?: false
            val residencePrefix = context.getString(R.string.residence)
            val residence = "$residencePrefix: ${user?.residence ?: ""}"
            residence(if (hasResidence) residence else context.getString(R.string.enter_residence))
            onResidenceClick { presenter.showResidenceDialog() }

            val hasLanguage = user?.hasLanguage ?: false
            val languagePrefix = context.getString(R.string.language)
            val language = "$languagePrefix: ${user?.language ?: ""}"
            language(if (hasLanguage) language else context.getString(R.string.enter_language))
            onLanguageClick { presenter.showLanguageDialog() }

            val hasEducation = user?.hasEducation ?: false
            val educationPrefix = context.getString(R.string.education)
            val education = "$educationPrefix: ${user?.education ?: ""}"
            education(if (hasEducation) education else context.getString(R.string.enter_education))
            onEducationClick { presenter.showEducationDialog() }

            val hasWork = user?.hasWork ?: false
            val workPrefix = context.getString(R.string.work)
            val work = "$workPrefix: ${user?.work ?: ""}"
            work(if (hasWork) work else context.getString(R.string.enter_work))
            onWorkClick { presenter.showWorkDialog() }

            val hasInterests = user?.hasInterests ?: false
            val interestsPrefix  = context.getString(R.string.interests)
            val interests = "$interestsPrefix: ${user?.interests ?: ""}"
            interests(if (hasInterests) interests else context.getString(R.string.enter_interests))
            onInterestsClick { presenter.showInterestsDialog() }
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
                { presenter.showToast(context.getString(R.string.add_own_offer_favorite_error)) },
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