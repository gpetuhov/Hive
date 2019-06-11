package com.gpetuhov.android.hive.util

import android.content.Context
import androidx.core.content.edit
import com.gpetuhov.android.hive.domain.model.Filter
import org.jetbrains.anko.defaultSharedPreferences

class Settings(context: Context) {

    companion object {
        private const val UNREAD_MESSAGES_EXIST_KEY = "unreadMessagesExist"
        private const val SELECTED_PHOTO_POSITION_KEY = "selectedPhotoPosition"
        private const val SEARCH_QUERY_TEXT_KEY = "searchQueryText"
        private const val SEARCH_FILTER_KEY = "searchFilter"
    }

    private val prefs = context.defaultSharedPreferences

    fun isUnreadMessagesExist() = prefs.getBoolean(UNREAD_MESSAGES_EXIST_KEY, false)

    fun setUnreadMessagesExist(value: Boolean) = prefs.edit { putBoolean(UNREAD_MESSAGES_EXIST_KEY, value) }

    fun getSelectedPhotoPosition() = prefs.getInt(SELECTED_PHOTO_POSITION_KEY, 0)

    fun setSelectedPhotoPosition(value: Int) = prefs.edit { putInt(SELECTED_PHOTO_POSITION_KEY, value) }

    fun getSearchQueryText() = prefs.getString(SEARCH_QUERY_TEXT_KEY, "") ?: ""

    fun setSearchQueryText(value: String) = prefs.edit { putString(SEARCH_QUERY_TEXT_KEY, value) }

    fun resetSearchQueryText() = setSearchQueryText("")

    fun getSearchFilter(): Filter {
        val filterJson = prefs.getString(SEARCH_FILTER_KEY, "") ?: ""
        val filter = Filter.fromJson(filterJson)
        return filter ?: Filter()
    }

    fun setSearchFilter(filter: Filter) {
        val filterJson = Filter.toJson(filter)
        prefs.edit { putString(SEARCH_FILTER_KEY, filterJson) }
    }

    fun resetSearchFilter() = setSearchFilter(Filter())
}