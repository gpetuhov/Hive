package com.gpetuhov.android.hive.domain.model

data class Photo(
    var uid: String,
    var downloadUrl: String,
    var isNew: Boolean = false,
    var isDeleted: Boolean = false
) {
    fun markAsNew() {
        isNew = true
        isDeleted = false
    }

    fun markAsDeleted() {
        isNew = false
        isDeleted = true
    }
}