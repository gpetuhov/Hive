package com.gpetuhov.android.hive.util

import android.content.Context
import androidx.core.content.edit
import org.jetbrains.anko.defaultSharedPreferences

class Settings(private val context: Context) {

    companion object {
        private const val SELECTED_PHOTO_POSITION_KEY = "selectedPhotoPosition"
    }

    fun getSelectedPhotoPosition() = context.defaultSharedPreferences.getInt(SELECTED_PHOTO_POSITION_KEY, 0)

    fun setSelectedPhotoPosition(value: Int) = context.defaultSharedPreferences.edit { putInt(SELECTED_PHOTO_POSITION_KEY, value) }
}