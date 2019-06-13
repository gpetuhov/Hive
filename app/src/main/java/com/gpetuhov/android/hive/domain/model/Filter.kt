package com.gpetuhov.android.hive.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi

class Filter(
    @field:Json(name = "freeOffersOnly") var isFreeOffersOnly: Boolean = false,
    @field:Json(name = "offersWithReviewsOnly") var isOffersWithReviewsOnly: Boolean = false,
    @field:Json(name = "hasPhone") var hasPhone: Boolean = false,
    @field:Json(name = "hasEmail") var hasEmail: Boolean = false,
    @field:Json(name = "hasSkype") var hasSkype: Boolean = false,
    @field:Json(name = "hasFacebook") var hasFacebook: Boolean = false,
    @field:Json(name = "hasTwitter") var hasTwitter: Boolean = false,
    @field:Json(name = "hasInstagram") var hasInstagram: Boolean = false,
    @field:Json(name = "hasYoutube") var hasYoutube: Boolean = false,
    @field:Json(name = "hasWebsite") var hasWebsite: Boolean = false,
    @field:Json(name = "hasTextMaster") var hasTextMaster: Boolean = false,
    @field:Json(name = "showUsersOffers") private var showUsersOffers: Int = SHOW_USERS_OFFERS_ALL
) {
    companion object {
        const val SHOW_USERS_OFFERS_ALL = 0
        const val SHOW_USERS_ONLY = 1
        const val SHOW_OFFERS_ONLY = 2

        fun toJson(filter: Filter): String {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Filter::class.java)

            return jsonAdapter.toJson(filter)
        }

        fun fromJson(json: String): Filter? {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Filter::class.java)

            return try {
                jsonAdapter.fromJson(json)
            } catch (e: Exception) {
                null
            }
        }
    }

    val isShowUsersOffersAll get() = showUsersOffers == SHOW_USERS_OFFERS_ALL
    val isShowUsersOnly get() = showUsersOffers == SHOW_USERS_ONLY
    val isShowOffersOnly get() = showUsersOffers == SHOW_OFFERS_ONLY

    val isDefault: Boolean get() {
        return showUsersOffers == SHOW_USERS_OFFERS_ALL
                && !isFreeOffersOnly
                && !isOffersWithReviewsOnly
                && !hasPhone
                && !hasEmail
                && !hasSkype
                && !hasFacebook
                && !hasTwitter
                && !hasInstagram
                && !hasYoutube
                && !hasWebsite
                && !hasTextMaster
    }

    // === Public methods ===

    fun setShowUsersOffersAll() {
        showUsersOffers = SHOW_USERS_OFFERS_ALL
    }

    fun setShowUsersOnly() {
        showUsersOffers = SHOW_USERS_ONLY
    }

    fun setShowOffersOnly() {
        showUsersOffers = SHOW_OFFERS_ONLY
    }
}