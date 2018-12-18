package com.gpetuhov.android.hive.models

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.Offer
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
            description = "",
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
    fun usernameOrName() {
        assertEquals(user.name, user.getUsernameOrName())
        user.username = DUMMY_USERNAME
        assertEquals(DUMMY_USERNAME, user.getUsernameOrName())
    }

    @Test
    fun emptyDescription() {
        assertEquals(false, user.hasDescription)
        user.description = "Dummy description"
        assertEquals(true, user.hasDescription)
    }

    @Test
    fun hasActiveOffer() {
        assertEquals(false, user.hasActiveOffer())

        user.offerList.add(Offer("", "pizza", "549gj", "tasty pizza", 10.0, false, false))
        assertEquals(false, user.hasActiveOffer())

        user.offerList.add(Offer("", "burger", "54984g", "yummy burgers", 5.0, false, true))
        assertEquals(true, user.hasActiveOffer())
    }

    @Test
    fun getOffer() {
        val dummyOffer = com.gpetuhov.android.hive.utils.Constants.DUMMY_OFFER
        assertEquals(null, user.getOffer(dummyOffer.uid))

        user.offerList.add(dummyOffer)
        assertEquals(dummyOffer, user.getOffer(dummyOffer.uid))
        assertEquals(null, user.getOffer("98gq"))
    }
}