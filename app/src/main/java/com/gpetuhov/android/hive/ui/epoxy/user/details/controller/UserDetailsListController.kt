package com.gpetuhov.android.hive.ui.epoxy.user.details.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.*
import com.gpetuhov.android.hive.util.Settings
import org.jetbrains.anko.collections.forEachWithIndex
import java.util.*
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    init {
        HiveApp.appComponent.inject(this)
        mapModel.onClick { presenter.openLocation() }
    }

    override fun buildModels() {
        val photoList = user?.photoList ?: mutableListOf()
        photoCarousel(
            settings,
            photoList,
            true,
            true,
            { photoUrlList -> presenter.openPhotos(photoUrlList) },
            { /* Do nothing */ }
        )

        userDetailsName {
            id("user_details_name")
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")

            val isOnline = user?.isOnline ?: false
            onlineVisible(isOnline)

            val lastSeen = user?.getLastSeenTime() ?: ""
            val lastSeenPrefix = if (lastSeen.length > 5) context.getString(R.string.last_seen) else context.getString(R.string.last_seen_at)
            lastSeen("$lastSeenPrefix $lastSeen")
            lastSeenVisible(!isOnline)
        }

        summary(context, false) { presenter.openAllReviews() }

        val hasPhone = user?.hasPhone ?: false
        val hasVisibleEmail = user?.hasVisibleEmail ?: false
        val hasSkype = user?.hasSkype ?: false
        val hasFacebook = user?.hasFacebook ?: false
        val hasTwitter = user?.hasTwitter ?: false
        val hasInstagram = user?.hasInstagram ?: false
        val hasYouTube = user?.hasYouTube ?: false
        val hasWebsite = user?.hasWebsite ?: false
        val hasContacts = hasPhone || hasVisibleEmail || hasSkype || hasFacebook || hasTwitter || hasInstagram || hasYouTube || hasWebsite
        if (hasContacts) {
            userDetailsContacts {
                id("user_details_contacts")

                val phone = user?.phone ?: ""
                phone(phone)
                phoneVisible(hasPhone)
                onPhoneClick { presenter.dialPhone(phone) }

                val email = user?.visibleEmail ?: ""
                email(email)
                emailVisible(hasVisibleEmail)
                emailSeparatorVisible(hasVisibleEmail && hasPhone)
                onEmailClick { presenter.sendEmail(email) }

                val skype = user?.skype ?: ""
                skype(skype)
                skypeVisible(hasSkype)
                skypeSeparatorVisible(hasSkype && (hasPhone || hasVisibleEmail))
                onSkypeClick { presenter.callSkype(skype) }

                val facebook = user?.facebook ?: ""
                facebook(facebook)
                facebookVisible(hasFacebook)
                facebookSeparatorVisible(hasFacebook && (hasPhone || hasVisibleEmail || hasSkype))
                onFacebookClick { presenter.openFacebook(facebook) }

                val twitter = user?.twitter ?: ""
                twitter(twitter)
                twitterVisible(hasTwitter)
                twitterSeparatorVisible(hasTwitter && (hasPhone || hasVisibleEmail || hasSkype || hasFacebook))
                onTwitterClick { presenter.openTwitter(twitter) }

                val instagram = user?.instagram ?: ""
                instagram(instagram)
                instagramVisible(hasInstagram)
                instagramSeparatorVisible(hasInstagram && (hasPhone || hasVisibleEmail || hasSkype || hasFacebook || hasTwitter))
                onInstagramClick { presenter.openInstagram(instagram) }

                val youTube = user?.youTube ?: ""
                youTube(youTube)
                youTubeVisible(hasYouTube)
                youTubeSeparatorVisible(hasYouTube && (hasPhone || hasVisibleEmail || hasSkype || hasFacebook || hasTwitter || hasInstagram))
                onYouTubeClick { presenter.openYouTube(youTube) }

                val website = user?.website ?: ""
                website(website)
                websiteVisible(hasWebsite)
                websiteSeparatorVisible(hasWebsite && (hasPhone || hasVisibleEmail || hasSkype || hasFacebook || hasTwitter || hasInstagram || hasYouTube))
                onWebsiteClick { presenter.openWebsite(website) }
            }
        }

        val hasDescription = user?.hasDescription ?: false
        if (hasDescription) {
            userDetailsDescription {
                id("user_details_description")
                description(user?.description ?: "")
            }
        }

        val hasResidence = user?.hasResidence ?: false
        val hasLanguage = user?.hasLanguage ?: false
        val hasEducation = user?.hasEducation ?: false
        val hasWork = user?.hasWork ?: false
        val hasInterests = user?.hasInterests ?: false
        val hasInformation = hasResidence || hasLanguage || hasEducation || hasWork || hasInterests
        if (hasInformation) {
            userDetailsInformation {
                id("user_details_information")

                val residencePrefix = context.getString(R.string.residence)
                val residence = "$residencePrefix: ${user?.residence ?: ""}"
                residence(residence)
                residenceVisible(hasResidence)

                val languagePrefix = context.getString(R.string.language)
                val language = "$languagePrefix: ${user?.language ?: ""}"
                language(language)
                languageVisible(hasLanguage)
                languageSeparatorVisible(hasLanguage && hasResidence)

                val educationPrefix = context.getString(R.string.education)
                val education = "$educationPrefix: ${user?.education ?: ""}"
                education(education)
                educationVisible(hasEducation)
                educationSeparatorVisible(hasEducation && (hasLanguage || hasResidence))

                val workPrefix = context.getString(R.string.work)
                val work = "$workPrefix: ${user?.work ?: ""}"
                work(work)
                workVisible(hasWork)
                workSeparatorVisible(hasWork && (hasLanguage || hasResidence || hasEducation))

                val interestsPrefix = context.getString(R.string.interests)
                val interests = "$interestsPrefix: ${user?.interests ?: ""}"
                interests(interests)
                interestsVisible(hasInterests)
                interestsSeparatorVisible(hasInterests && (hasLanguage || hasResidence || hasEducation || hasWork))
            }
        }

        userDetailsOfferHeader {
            id("user_details_offer_header")

            val hasActiveOffer = user?.hasActiveOffer() ?: false
            offerHeader(if (hasActiveOffer) context.getString(R.string.offers) else context.getString(R.string.no_active_offer))
        }

        user?.offerList?.forEach { offer ->
            // In user details show only active offers
            if (offer.isActive) {
                userOfferItem(
                    context,
                    settings,
                    offer,
                    false,
                    true,
                    { presenter.favoriteOffer(offer.isFavorite, offer.uid) },
                    { presenter.openOffer(offer.uid) }
                )
            }
        }

        reviewsHeader()

        user?.offerList?.sortedByDescending { it.lastReviewTimestamp }?.forEachWithIndex { index, offer ->
            if (offer.isActive) lastOfferReview(offer, index)
        }

        reviewsSummary(context, false) { presenter.openAllReviews() }

        // MapModel will be bind here only once (after fragment creation),
        // because location is not annotated as epoxy attribute.
        mapModel.addTo(this)
    }

    override fun changeUser(user: User) {
        // Manually update MapModel with new location.
        // If the model is already bind, map will be updated.
        // Otherwise map will be updated, when the model is bind.
        mapModel.updateMap(user.location)

        super.changeUser(user)
    }
}