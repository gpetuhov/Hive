package com.gpetuhov.android.hive.domain.model

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.getLastSeenTimeFromTimestamp

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

    val hasTextMasterAward get() = photoList.isNotEmpty() && (userPicUrl != "") && hasUsername && hasDescription
            && hasPhone && hasVisibleEmail && hasSkype && hasFacebook && hasTwitter && hasInstagram
            && hasYouTube && hasWebsite && hasResidence && hasLanguage && hasEducation && hasWork
            && hasInterests && hasStatus

    val hasOfferProviderAward get() = firstOfferPublishedTimestamp != 0L

    fun hasActiveOffer() = offerList.any { it.isActive }

    fun getUsernameOrName() = if (hasUsername) username else name

    fun getOffer(offerUid: String) = offerList.firstOrNull { it.uid == offerUid }

    fun getLastSeenTime() = getLastSeenTimeFromTimestamp(lastSeen)

    fun getNewAwards(): MutableList<Int> {
        val newAwardsList = mutableListOf<Int>()
        if (hasTextMasterAward && !isTextMasterCongratulationShown()) newAwardsList.add(Constants.Award.TEXT_MASTER)
        if (hasOfferProviderAward && !isOfferProviderCongratulationShown()) newAwardsList.add(Constants.Award.OFFER_PROVIDER)
        return newAwardsList
    }

    // === Private methods ===

    private fun isTextMasterCongratulationShown() = awardCongratulationShownList.contains(Constants.Award.TEXT_MASTER)

    private fun isOfferProviderCongratulationShown() = awardCongratulationShownList.contains(Constants.Award.OFFER_PROVIDER)
}