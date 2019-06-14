package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Sort
import org.junit.Assert.assertEquals
import org.junit.Test

class SortTest {

    companion object {
        private const val SORT_PARAM_KEY = "sortParam"
        private const val SORT_ORDER_ASCENDING_KEY = "sortOrderAscending"
        private const val SORT_OFFERS_FIRST_KEY = "sortOffersFirst"
    }

    @Test
    fun sortByTitle() {
        val sort = Sort()
        assertEquals(true, sort.isSortByTitle)
        sort.setSortByPrice()
        assertEquals(false, sort.isSortByTitle)
        sort.setSortByTitle()
        assertEquals(true, sort.isSortByTitle)
    }

    @Test
    fun sortByPrice() {
        val sort = Sort()
        assertEquals(false, sort.isSortByPrice)
        sort.setSortByPrice()
        assertEquals(true, sort.isSortByPrice)
    }

    @Test
    fun sortByRating() {
        val sort = Sort()
        assertEquals(false, sort.isSortByRating)
        sort.setSortByRating()
        assertEquals(true, sort.isSortByRating)
    }

    @Test
    fun toJson() {
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_TITLE) { it.setSortByTitle() }
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_PRICE) { it.setSortByPrice() }
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_RATING) { it.setSortByRating() }
        checkToJson(SORT_ORDER_ASCENDING_KEY, true) { it.isSortOrderAscending = true }
        checkToJson(SORT_ORDER_ASCENDING_KEY, false) { it.isSortOrderAscending = false }
        checkToJson(SORT_OFFERS_FIRST_KEY, true) { it.isSortOffersFirst = true }
        checkToJson(SORT_OFFERS_FIRST_KEY, false) { it.isSortOffersFirst = false }
    }

    @Test
    fun fromJson() {
        checkFromJson({ it.setSortByTitle() }, { it.isSortByTitle })
        checkFromJson({ it.setSortByPrice() }, { it.isSortByPrice })
        checkFromJson({ it.setSortByRating() }, { it.isSortByRating })
        checkFromJson({ it.isSortOrderAscending = true }, { it.isSortOrderAscending })
        checkFromJson({ it.isSortOrderAscending = false }, { !it.isSortOrderAscending })
        checkFromJson({ it.isSortOffersFirst = true }, { it.isSortOffersFirst })
        checkFromJson({ it.isSortOffersFirst = false }, { !it.isSortOffersFirst })
    }

    @Test
    fun defaultFilter() {
        checkIsDefault()
        checkIsNotDefault { it.setSortByPrice() }
        checkIsNotDefault { it.isSortOrderAscending = false }
        checkIsNotDefault { it.isSortOffersFirst = false }
    }

    // === Private methods ===

    private fun checkToJson(expectedJsonKey: String, expectedJsonValue: Any, action: (Sort) -> Unit) {
        val sort = Sort()
        action(sort)
        val json = Sort.toJson(sort)
        val expectedJsonString = "\"$expectedJsonKey\":$expectedJsonValue"
        assertEquals(true, json.contains(expectedJsonString))
    }

    private fun checkFromJson(action: (Sort) -> Unit, assertion: (Sort) -> Unit) {
        val sourceSort = Sort()
        action(sourceSort)
        val json = Sort.toJson(sourceSort)
        val resultSort = Sort.fromJson(json)
        assertEquals(true, resultSort != null)
        if (resultSort != null) assertion(resultSort)
    }

    private fun checkIsDefault() {
        val sort = Sort()
        assertEquals(true, sort.isDefault)
    }

    private fun checkIsNotDefault(action: (Sort) -> Unit) {
        val sort = Sort()
        action(sort)
        assertEquals(false, sort.isDefault)
    }
}