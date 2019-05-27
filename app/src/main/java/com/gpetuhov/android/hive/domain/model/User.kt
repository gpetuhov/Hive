package com.gpetuhov.android.hive.domain.model

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.getLastSeenTimeFromTimestamp
import java.util.*

// Represents data both for the current user and search results.
// Models at domain layer are just POJOs for keeping data.
data class User(
    var uid: String,
    var name: String,
    var username: String,
    var email: String,
    var userPicUrl: String,
    var description: String,
    var isOnline: Boolean,
    var location: LatLng,
    var offerSearchResultIndex: Int = -1,    // index of the offer that corresponds to search query text
    var isFavorite: Boolean = false,
    var creationTimestamp: Long = 0,
    var firstOfferPublishedTimestamp: Long = 0,
    var phone: String = "",
    var visibleEmail: String = "",
    var skype: String = "",
    var facebook: String = "",
    var twitter: String = "",
    var instagram: String = "",
    var youTube: String = "",
    var website: String = "",
    var residence: String = "",
    var language: String = "",
    var education: String = "",
    var work: String = "",
    var interests: String = "",
    var lastSeen: Long = 0L,
    var status: String = "",
    var activity: Long = Constants.User.NO_ACTIVITY,
    var isDeleted: Boolean = false
) {
    var offerList = mutableListOf<Offer>()
    var photoList = mutableListOf<Photo>()

    // This list contains awards, for which congratulations have been already SHOWN to the user
    var awardCongratulationShownList = mutableListOf<Int>()

    val hasUsername get() = username != ""
    val hasDescription get() = description != ""
    val hasPhone get() = phone != ""
    val hasVisibleEmail get() = visibleEmail != ""
    val hasSkype get() = skype != ""
    val hasFacebook get() = facebook != ""
    val hasTwitter get() = twitter != ""
    val hasInstagram get() = instagram != ""
    val hasYouTube get() = youTube != ""
    val hasWebsite get() = website != ""
    val hasResidence get() = residence != ""
    val hasLanguage get() = language != ""
    val hasEducation get() = education != ""
    val hasWork get() = work != ""
    val hasInterests get() = interests != ""
    val hasStatus get() = status != ""
    val hasActivity get() = activity != Constants.User.NO_ACTIVITY

    val awardsList = mutableListOf<Int>()
    val newAwardsList = mutableListOf<Int>()
    val awardTipsList = mutableListOf<Int>()

    var activeOfferList = mutableListOf<Offer>()
    var totalReviewsCount = 0
    var averageRating = 0F

    val hasTextMasterAward get() = photoList.isNotEmpty() && (userPicUrl != "") && hasUsername && hasDescription
            && hasPhone && hasVisibleEmail && hasSkype && hasFacebook && hasTwitter && hasInstagram
            && hasYouTube && hasWebsite && hasResidence && hasLanguage && hasEducation && hasWork
            && hasInterests && hasStatus

    val hasOfferProviderAward get() = firstOfferPublishedTimestamp != 0L

    var hasAltruistAward = false

    val hasGoodProviderAward get() = totalReviewsCount >= Constants.Awards.GOOD_PROVIDER_AWARD_MIN_REVIEW_COUNT
            && totalReviewsCount < Constants.Awards.SUPER_PROVIDER_AWARD_MIN_REVIEW_COUNT
            && averageRating >= Constants.Awards.GOOD_PROVIDER_AWARD_MIN_RATING

    val hasSuperProviderAward get() = totalReviewsCount >= Constants.Awards.SUPER_PROVIDER_AWARD_MIN_REVIEW_COUNT
            && averageRating >= Constants.Awards.SUPER_PROVIDER_AWARD_MIN_RATING

    val hasReviewedProviderAward get() = totalReviewsCount > 0

    fun hasActiveOffer() = offerList.any { it.isActive }

    fun getUsernameOrName() = if (hasUsername) username else name

    fun getOffer(offerUid: String) = offerList.firstOrNull { it.uid == offerUid }

    fun getLastSeenTime() = getLastSeenTimeFromTimestamp(lastSeen)

    fun hasNewbieAward(): Boolean {
        val daysInHive = (Date().time - creationTimestamp) / (1000 * 60 * 60 * 24.0)
        return daysInHive <= Constants.Awards.NEWBIE_AWARD_DAYS_LIMIT
    }

    fun updateAwards() {
        awardsList.clear()
        newAwardsList.clear()
        awardTipsList.clear()

        calculateRatings()

        // Awards in awardsList and newAwardsList will be sorted from difficult to easy.
        // Awards in awardTipsList will be sorted from easy to difficult.

        // SuperProvider Award has no tip
        val superProviderId = Constants.Awards.SUPER_PROVIDER_ID
        if (hasSuperProviderAward) {
            awardsList.add(superProviderId)
            if (!(awardCongratulationShownList.contains(superProviderId))) newAwardsList.add(superProviderId)
        }

        // GoodProvider Award has no tip
        val goodProviderId = Constants.Awards.GOOD_PROVIDER_ID
        if (hasGoodProviderAward) {
            awardsList.add(goodProviderId)
            if (!(awardCongratulationShownList.contains(goodProviderId))) newAwardsList.add(goodProviderId)
        }

        val reviewedProviderId = Constants.Awards.REVIEWED_PROVIDER_ID
        if (hasReviewedProviderAward) {
            awardsList.add(reviewedProviderId)
            if (!(awardCongratulationShownList.contains(reviewedProviderId))) newAwardsList.add(reviewedProviderId)
        } else {
            awardTipsList.add(0, reviewedProviderId)
        }

        // Has Altruist award if 3 or more active offers and all active offers are free.
        val freeActiveOfferList = activeOfferList.filter { it.isFree }
        hasAltruistAward = (activeOfferList.size >= Constants.Offer.MAX_ACTIVE_OFFER_COUNT) && (activeOfferList.size == freeActiveOfferList.size)
        val altruistId = Constants.Awards.ALTRUIST_ID
        if (hasAltruistAward) {
            awardsList.add(altruistId)
            if (!(awardCongratulationShownList.contains(altruistId))) newAwardsList.add(altruistId)
        } else {
            awardTipsList.add(0, altruistId)
        }

        val offerProviderId = Constants.Awards.OFFER_PROVIDER_ID
        if (hasOfferProviderAward) {
            awardsList.add(offerProviderId)
            if (!(awardCongratulationShownList.contains(offerProviderId))) newAwardsList.add(offerProviderId)
        } else {
            awardTipsList.add(0, offerProviderId)
        }

        val textMasterId = Constants.Awards.TEXT_MASTER_ID
        if (hasTextMasterAward) {
            awardsList.add(textMasterId)
            if (!(awardCongratulationShownList.contains(textMasterId))) newAwardsList.add(textMasterId)
        } else {
            awardTipsList.add(0, textMasterId)
        }

        // Newbie Award has no congratulation and tip
        if (hasNewbieAward()) {
            awardsList.add(Constants.Awards.NEWBIE_ID)
        }
    }

    // === Private methods ===

    private fun calculateRatings() {
        activeOfferList = offerList.filter { it.isActive }.toMutableList()

        totalReviewsCount = 0
        activeOfferList.forEach { totalReviewsCount += it.reviewCount }

        averageRating = 0.0F
        val activeOfferWithReviewsList = activeOfferList.filter { it.reviewCount > 0 }
        activeOfferWithReviewsList.forEach { averageRating += it.rating }
        val activeOfferWithReviewsCount = activeOfferWithReviewsList.size
        if (activeOfferWithReviewsCount > 0) averageRating /= activeOfferWithReviewsCount
    }
}