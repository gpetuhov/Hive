package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.utils.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PhotoTest {

    private lateinit var photo: Photo

    @Before
    fun init() {
        photo = Constants.DUMMY_PHOTO
    }

    @Test
    fun defaultFlagsFalse() {
        assertEquals(false, photo.isNew)
        assertEquals(false, photo.isDeleted)
    }

    @Test
    fun markPhotoNew() {
        photo.markAsNew()
        assertEquals(true, photo.isNew)
        assertEquals(false, photo.isDeleted)
    }

    @Test
    fun markPhotoDeleted() {
        photo.markAsDeleted()
        assertEquals(false, photo.isNew)
        assertEquals(true, photo.isDeleted)
    }

    @Test
    fun markPhotoNewDeleted() {
        photo.markAsNew()
        photo.markAsDeleted()
        assertEquals(false, photo.isNew)
        assertEquals(true, photo.isDeleted)
    }
}