package com.gpetuhov.android.hive.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi

class Sort(
    @field:Json(name = "sortOrderAscending") var isSortOrderAscending: Boolean = true,
    @field:Json(name = "sortOffersFirst") var isSortOffersFirst: Boolean = true,
    @field:Json(name = "sortParam") private var sortParam: Int = SORT_BY_TITLE
) {
    companion object {
        const val SORT_BY_TITLE = 0
        const val SORT_BY_PRICE = 1
        const val SORT_BY_RATING = 2

        fun toJson(sort: Sort): String {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Sort::class.java)

            return jsonAdapter.toJson(sort)
        }

        fun fromJson(json: String): Sort? {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Sort::class.java)

            return try {
                jsonAdapter.fromJson(json)
            } catch (e: Exception) {
                null
            }
        }
    }

    val isSortByTitle get() = sortParam == SORT_BY_TITLE
    val isSortByPrice get() = sortParam == SORT_BY_PRICE
    val isSortByRating get() = sortParam == SORT_BY_RATING

    val isDefault: Boolean get() {
        return sortParam == SORT_BY_TITLE
                && isSortOrderAscending
                && isSortOffersFirst
    }

    // === Public methods ===

    fun setSortByTitle() {
        sortParam = SORT_BY_TITLE
    }

    fun setSortByPrice() {
        sortParam = SORT_BY_PRICE
    }

    fun setSortByRating() {
        sortParam = SORT_BY_RATING
    }
}