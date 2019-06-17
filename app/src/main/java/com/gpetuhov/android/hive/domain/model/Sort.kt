package com.gpetuhov.android.hive.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi

class Sort(
    @field:Json(name = "sortOrderAscending") var isSortOrderAscending: Boolean = true,
    @field:Json(name = "sortOffersFirst") var isSortOffersFirst: Boolean = true,
    @field:Json(name = "sortParam") private var sortParam: Int = SORT_BY_DISTANCE
) {
    companion object {
        const val SORT_BY_DISTANCE = 0
        const val SORT_BY_TITLE = 1
        const val SORT_BY_PRICE = 2
        const val SORT_BY_RATING = 3
        const val SORT_BY_REVIEW_COUNT = 4
        const val SORT_BY_FAVORITE_STAR_COUNT = 5
        const val SORT_BY_PHOTO_COUNT = 6

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

    val isSortByDistance get() = sortParam == SORT_BY_DISTANCE
    val isSortByTitle get() = sortParam == SORT_BY_TITLE
    val isSortByPrice get() = sortParam == SORT_BY_PRICE
    val isSortByRating get() = sortParam == SORT_BY_RATING
    val isSortByReviewCount get() = sortParam == SORT_BY_REVIEW_COUNT
    val isSortByFavoriteStarCount get() = sortParam == SORT_BY_FAVORITE_STAR_COUNT
    val isSortByPhotoCount get() = sortParam == SORT_BY_PHOTO_COUNT

    val isDefault: Boolean get() {
        return sortParam == SORT_BY_DISTANCE
                && isSortOrderAscending
                && isSortOffersFirst
    }

    // === Public methods ===

    fun setSortByDistance() {
        sortParam = SORT_BY_DISTANCE
    }

    fun setSortByTitle() {
        sortParam = SORT_BY_TITLE
    }

    fun setSortByPrice() {
        sortParam = SORT_BY_PRICE
    }

    fun setSortByRating() {
        sortParam = SORT_BY_RATING
    }

    fun setSortByReviewCount() {
        sortParam = SORT_BY_REVIEW_COUNT
    }

    fun setSortByFavoriteStarCount() {
        sortParam = SORT_BY_FAVORITE_STAR_COUNT
    }

    fun setSortByPhotoCount() {
        sortParam = SORT_BY_PHOTO_COUNT
    }
}