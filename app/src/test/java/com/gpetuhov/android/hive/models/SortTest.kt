package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Sort
import org.junit.Assert.assertEquals
import org.junit.Test

class SortTest {

    companion object {
        private const val SORT_PARAM_KEY = "sortParam"
        private const val SORT_ORDER_KEY = "sortOrder"
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
    fun sortOrderAscending() {
        val sort = Sort()
        assertEquals(true, sort.isSortOrderAscending)
        sort.setSortOrderDescending()
        assertEquals(false, sort.isSortOrderAscending)
        sort.setSortOrderAscending()
        assertEquals(true, sort.isSortOrderAscending)
    }

    @Test
    fun sortOrderDescending() {
        val sort = Sort()
        assertEquals(false, sort.isSortOrderDescending)
        sort.setSortOrderDescending()
        assertEquals(true, sort.isSortOrderDescending)
    }

    @Test
    fun toJson() {
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_TITLE) { it.setSortByTitle() }
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_PRICE) { it.setSortByPrice() }
        checkToJson(SORT_PARAM_KEY, Sort.SORT_BY_RATING) { it.setSortByRating() }
        checkToJson(SORT_ORDER_KEY, Sort.SORT_ORDER_ASCENDING) { it.setSortOrderAscending() }
        checkToJson(SORT_ORDER_KEY, Sort.SORT_ORDER_DESCENDING) { it.setSortOrderDescending() }
    }

    @Test
    fun fromJson() {
        checkFromJson({ it.setSortByTitle() }, { it.isSortByTitle })
        checkFromJson({ it.setSortByPrice() }, { it.isSortByPrice })
        checkFromJson({ it.setSortByRating() }, { it.isSortByRating })
        checkFromJson({ it.setSortOrderAscending() }, { it.isSortOrderAscending })
        checkFromJson({ it.setSortOrderDescending() }, { it.isSortOrderDescending })
    }

    @Test
    fun defaultFilter() {
        checkIsDefault()
        checkIsNotDefault { it.setSortByPrice() }
        checkIsNotDefault { it.setSortOrderDescending() }
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