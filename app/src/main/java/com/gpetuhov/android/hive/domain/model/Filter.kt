package com.gpetuhov.android.hive.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi

class Filter(
    @Json(name = "showUsersOffers") private var showUsersOffers: Int = SHOW_USERS_OFFERS_ALL
) {
    companion object {
        private const val SHOW_USERS_OFFERS_ALL = 0
        private const val SHOW_USERS_ONLY = 1
        private const val SHOW_OFFERS_ONLY = 2

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