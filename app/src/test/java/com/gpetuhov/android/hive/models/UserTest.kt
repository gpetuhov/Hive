package com.gpetuhov.android.hive.models

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.Constants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserTest {

    companion object {
        const val DUMMY_USERNAME = "dummy_username"
    }

    private lateinit var user: User

    @Before
    fun initUser() {
        user = User(
            uid = "342gg34g2",
            name = "John",
            username = "",
            email = "mail@mail.com",
            userPicUrl = "",
            offer = "",
            isVisible = false,
            isOnline = false,
            location = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)
        )
    }

    @Test
    fun emptyUsername() {
        assertEquals(false, user.hasUsername)
        user.username = DUMMY_USERNAME
        assertEquals(true, user.hasUsername)
    }

    @Test
    fun emptyService() {
        assertEquals(false, user.hasOffer)
        user.offer = "free car"
        assertEquals(true, user.hasOffer)
    }

    @Test
    fun usernameOrName() {
        assertEquals(user.name, user.getUsernameOrName())
        user.username = DUMMY_USERNAME
        assertEquals(DUMMY_USERNAME, user.getUsernameOrName())
    }
}