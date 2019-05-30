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
    var isDeleted: Boolean = false,
    var postedReviewsCount: Long = 0L,   // This is how many reviews THIS user posted
    var postedFirstReviewsCount: Long = 0L,      // This is how many FIRST reviews this user posted
    var userStarCount: Long = 0L    // This is how many stars the user has (how many times user has been added to favorites)
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
    val userStarCountString get() = userStarCount.toString()

    val awardsList = mutableListOf<Int>()
    val newAwardsList = mutableListOf<Int>()
    val awardTipsList = mutableListOf<Int>()

    var activeOfferList = mutableListOf<Offer>()
    var totalReviewsCount = 0   // This is how many reviews OTHER users posted on this user's offers
    var averageRating = 0F

    var totalStarCount = 0L // This is total stars that has this user plus all of his active offers

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

    // User can have HiveCore award on paid subscription only (because free subscription has active offer limit)
    val hasHiveCoreAward get() = activeOfferList.size >= Constants.Awards.HIVECORE_AWARD_MIN_ACTIVE_OFFER_COUNT

    val hasReviewPosterAward get() = postedReviewsCount > 0

    val hasStoryTellerAward get() = postedReviewsCount >= Constants.Awards.STORY_TELLER_AWARD_MIN_REVIEW_COUNT

    val hasMegaCriticAward get() = postedReviewsCount >= Constants.Awards.MEGA_CRITIC_AWARD_MIN_REVIEW_COUNT

    val hasOfferFinderAward get() = postedFirstReviewsCount > 0

    val hasOflumbusAward get() = postedFirstReviewsCount >= Constants.Awards.OFLUMBUS_AWARD_MIN_REVIEW_COUNT

    val hasFavoriteProviderAward get() = totalStarCount > 0

    val hasAdorableProviderAward get() = totalStarCount >= Constants.Awards.ADORABLE_PROVIDER_AWARD_MIN_STAR_COUNT

    val hasRockStarAward get() = totalStarCount >= Constants.Awards.ROCK_STAR_AWARD_MIN_STAR_COUNT

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

        // ReviewedProvider Award has no tip
        val reviewedProviderId = Constants.Awards.REVIEWED_PROVIDER_ID
        if (hasReviewedProviderAward) {
            awardsList.add(reviewedProviderId)
            if (!(awardCongratulationShownList.contains(reviewedProviderId))) newAwardsList.add(reviewedProviderId)
        }

        // RockStar Award has no tip
        val rockStarId = Constants.Awards.ROCK_STAR_ID
        if (hasRockStarAward) {
            awardsList.add(rockStarId)
            if (!(awardCongratulationShownList.contains(rockStarId))) newAwardsList.add(rockStarId)
        }

        // AdorableProvider Award has no tip
        val adorableProviderId = Constants.Awards.ADORABLE_PROVIDER_ID
        if (hasAdorableProviderAward) {
            awardsList.add(adorableProviderId)
            if (!(awardCongratulationShownList.contains(adorableProviderId))) newAwardsList.add(adorableProviderId)
        }

        // FavoriteProvider Award has no tip
        val favoriteProviderId = Constants.Awards.FAVORITE_PROVIDER_ID
        if (hasFavoriteProviderAward) {
            awardsList.add(favoriteProviderId)
            if (!(awardCongratulationShownList.contains(favoriteProviderId))) newAwardsList.add(favoriteProviderId)
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

        // HiveCore Award should have tip when paid subscription implemented only
        // TODO: add HiveCore Award tip, when paid subscription implemented
        val hiveCoreId = Constants.Awards.HIVECORE_ID
        if (hasHiveCoreAward) {
            awardsList.add(hiveCoreId)
            if (!(awardCongratulationShownList.contains(hiveCoreId))) newAwardsList.add(hiveCoreId)
        }

        val oflumbusId = Constants.Awards.OFLUMBUS_ID
        if (hasOflumbusAward) {
            awardsList.add(oflumbusId)
            if (!(awardCongratulationShownList.contains(oflumbusId))) newAwardsList.add(oflumbusId)
        } else {
            awardTipsList.add(0, oflumbusId)
        }

        val offerFinderId = Constants.Awards.OFFER_FINDER_ID
        if (hasOfferFinderAward) {
            awardsList.add(offerFinderId)
            if (!(awardCongratulationShownList.contains(offerFinderId))) newAwardsList.add(offerFinderId)
        } else {
            awardTipsList.add(0, offerFinderId)
        }

        val megaCriticId = Constants.Awards.MEGA_CRITIC_ID
        if (hasMegaCriticAward) {
            awardsList.add(megaCriticId)
            if (!(awardCongratulationShownList.contains(megaCriticId))) newAwardsList.add(megaCriticId)
        } else {
            awardTipsList.add(0, megaCriticId)
        }

        val storyTellerId = Constants.Awards.STORY_TELLER_ID
        if (hasStoryTellerAward) {
            awardsList.add(storyTellerId)
            if (!(awardCongratulationShownList.contains(storyTellerId))) newAwardsList.add(storyTellerId)
        } else {
            awardTipsList.add(0, storyTellerId)
        }

        val reviewPosterId = Constants.Awards.REVIEW_POSTER_ID
        if (hasReviewPosterAward) {
            awardsList.add(reviewPosterId)
            if (!(awardCongratulationShownList.contains(reviewPosterId))) newAwardsList.add(reviewPosterId)
        } else {
            awardTipsList.add(0, reviewPosterId)
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
        totalStarCount = userStarCount
        activeOfferList.forEach {
            totalReviewsCount += it.reviewCount
            totalStarCount += it.starCount
        }

        averageRating = 0.0F
        val activeOfferWithReviewsList = activeOfferList.filter { it.reviewCount > 0 }
        activeOfferWithReviewsList.forEach { averageRating += it.rating }
        val activeOfferWithReviewsCount = activeOfferWithReviewsList.size
        if (activeOfferWithReviewsCount > 0) averageRating /= activeOfferWithReviewsCount
    }
}