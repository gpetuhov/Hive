package com.gpetuhov.android.hive.models

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserTest {

    private lateinit var user: User

    @Before
    fun initUser() {
        user = User(
            "342gg34g2",
            "John",
            "",
            "mail@mail.com",
            "free car",
            false,
            LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)
        )
    }

    @Test
    fun emptyUsername() {
        assertEquals(false, user.hasUsername)
        user.username = "john7"
        assertEquals(true, user.hasUsername)
    }
}