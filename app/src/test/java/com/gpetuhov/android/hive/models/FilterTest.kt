package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Filter
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterTest {

    companion object {
        private const val SHOW_USERS_OFFERS_KEY = "showUsersOffers"
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
        val filter = Filter()
        checkToJson(filter, SHOW_USERS_OFFERS_KEY, Filter.SHOW_USERS_OFFERS_ALL) { filter.setShowUsersOffersAll() }
        checkToJson(filter, SHOW_USERS_OFFERS_KEY, Filter.SHOW_USERS_ONLY) { filter.setShowUsersOnly() }
        checkToJson(filter, SHOW_USERS_OFFERS_KEY, Filter.SHOW_OFFERS_ONLY) { filter.setShowOffersOnly() }
    }

    // === Private methods ===

    private fun checkToJson(filter: Filter, expectedJsonKey: String, expectedJsonValue: Any, action: () -> Unit) {
        action()
        val json = Filter.toJson(filter)
        val expectedJsonString = "\"$expectedJsonKey\":$expectedJsonValue}"
        assertEquals(true, json.contains(expectedJsonString))
    }
}