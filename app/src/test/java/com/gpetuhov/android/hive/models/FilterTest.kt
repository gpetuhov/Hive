package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Filter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterTest {

    private lateinit var filter: Filter

    @Before
    fun initFilter() {
        filter = Filter()
    }

    @Test
    fun showUsersOffersAll() {
        assertEquals(true, filter.isShowUsersOffersAll)
        filter.setShowUsersOnly()
        assertEquals(false, filter.isShowUsersOffersAll)
        filter.setShowUsersOffersAll()
        assertEquals(true, filter.isShowUsersOffersAll)
    }

    @Test
    fun showUsersOnly() {
        assertEquals(false, filter.isShowUsersOnly)
        filter.setShowUsersOnly()
        assertEquals(true, filter.isShowUsersOnly)
    }

    @Test
    fun showOffersOnly() {
        assertEquals(false, filter.isShowOffersOnly)
        filter.setShowOffersOnly()
        assertEquals(true, filter.isShowOffersOnly)
    }

    @Test
    fun toJson() {
        val showUsersOffersKey = "\"showUsersOffers\""
        var json = Filter.toJson(filter)
        var expectedJsonValue = "$showUsersOffersKey:${Filter.SHOW_USERS_OFFERS_ALL}"
        assertEquals(true, json.contains(expectedJsonValue))

        filter.setShowUsersOnly()
        json = Filter.toJson(filter)
        expectedJsonValue = "$showUsersOffersKey:${Filter.SHOW_USERS_ONLY}"
        assertEquals(true, json.contains(expectedJsonValue))

        filter.setShowOffersOnly()
        json = Filter.toJson(filter)
        expectedJsonValue = "$showUsersOffersKey:${Filter.SHOW_OFFERS_ONLY}"
        assertEquals(true, json.contains(expectedJsonValue))
    }
}