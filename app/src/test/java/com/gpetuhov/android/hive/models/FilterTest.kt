package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Filter
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterTest {

    companion object {
        private const val SHOW_USERS_OFFERS_KEY = "showUsersOffers"
        private const val FREE_OFFERS_ONLY_KEY = "freeOffersOnly"
        private const val OFFERS_WITH_REVIEWS_ONLY_KEY = "offersWithReviewsOnly"
    }

    @Test
    fun showUsersOffersAll() {
        val filter = Filter()
        assertEquals(true, filter.isShowUsersOffersAll)
        filter.setShowUsersOnly()
        assertEquals(false, filter.isShowUsersOffersAll)
        filter.setShowUsersOffersAll()
        assertEquals(true, filter.isShowUsersOffersAll)
    }

    @Test
    fun showUsersOnly() {
        val filter = Filter()
        assertEquals(false, filter.isShowUsersOnly)
        filter.setShowUsersOnly()
        assertEquals(true, filter.isShowUsersOnly)
    }

    @Test
    fun showOffersOnly() {
        val filter = Filter()
        assertEquals(false, filter.isShowOffersOnly)
        filter.setShowOffersOnly()
        assertEquals(true, filter.isShowOffersOnly)
    }

    @Test
    fun toJson() {
        checkToJson(SHOW_USERS_OFFERS_KEY, Filter.SHOW_USERS_OFFERS_ALL) { it.setShowUsersOffersAll() }
        checkToJson(SHOW_USERS_OFFERS_KEY, Filter.SHOW_USERS_ONLY) { it.setShowUsersOnly() }
        checkToJson(SHOW_USERS_OFFERS_KEY, Filter.SHOW_OFFERS_ONLY) { it.setShowOffersOnly() }
        checkToJson(FREE_OFFERS_ONLY_KEY, true) { it.isFreeOffersOnly = true }
        checkToJson(OFFERS_WITH_REVIEWS_ONLY_KEY, true) { it.isOffersWithReviewsOnly = true }
    }

    @Test
    fun fromJson() {
        checkFromJson({ it.setShowUsersOffersAll() }, { it.isShowUsersOffersAll })
        checkFromJson({ it.setShowUsersOnly() }, { it.isShowUsersOnly })
        checkFromJson({ it.setShowOffersOnly() }, { it.isShowOffersOnly })
        checkFromJson({ it.isFreeOffersOnly = true }, { it.isFreeOffersOnly })
        checkFromJson({ it.isOffersWithReviewsOnly = true }, { it.isOffersWithReviewsOnly })
    }

    @Test
    fun defaultFilter() {
        checkIsDefault()
        checkIsNotDefault { it.setShowUsersOnly() }
        checkIsNotDefault { it.isFreeOffersOnly = true }
        checkIsNotDefault { it.isOffersWithReviewsOnly = true }
    }

    // === Private methods ===

    private fun checkToJson(expectedJsonKey: String, expectedJsonValue: Any, action: (Filter) -> Unit) {
        val filter = Filter()
        action(filter)
        val json = Filter.toJson(filter)
        val expectedJsonString = "\"$expectedJsonKey\":$expectedJsonValue"
        assertEquals(true, json.contains(expectedJsonString))
    }

    private fun checkFromJson(action: (Filter) -> Unit, assertion: (Filter) -> Unit) {
        val sourceFilter = Filter()
        action(sourceFilter)
        val json = Filter.toJson(sourceFilter)
        val resultFilter = Filter.fromJson(json)
        assertEquals(true, resultFilter != null)
        if (resultFilter != null) assertion(resultFilter)
    }

    private fun checkIsDefault() {
        val filter = Filter()
        assertEquals(true, filter.isDefault)
    }

    private fun checkIsNotDefault(action: (Filter) -> Unit) {
        val filter = Filter()
        action(filter)
        assertEquals(false, filter.isDefault)
    }
}