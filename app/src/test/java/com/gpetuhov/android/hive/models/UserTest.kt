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

    @Test
    fun emptyPhone() {
        assertEquals(false, user.hasPhone)
        user.phone = com.gpetuhov.android.hive.utils.Constants.DUMMY_PHONE
        assertEquals(true, user.hasPhone)
    }

    @Test
    fun emptyVisibleEmail() {
        assertEquals(false, user.hasVisibleEmail)
        user.visibleEmail = com.gpetuhov.android.hive.utils.Constants.DUMMY_EMAIL
        assertEquals(true, user.hasVisibleEmail)
    }

    @Test
    fun emptySkype() {
        assertEquals(false, user.hasSkype)
        user.skype = com.gpetuhov.android.hive.utils.Constants.DUMMY_SKYPE
        assertEquals(true, user.hasSkype)
    }

    @Test
    fun emptyFacebook() {
        assertEquals(false, user.hasFacebook)
        user.facebook = com.gpetuhov.android.hive.utils.Constants.DUMMY_FACEBOOK
        assertEquals(true, user.hasFacebook)
    }

    @Test
    fun emptyTwitter() {
        assertEquals(false, user.hasTwitter)
        user.twitter = com.gpetuhov.android.hive.utils.Constants.DUMMY_TWITTER
        assertEquals(true, user.hasTwitter)
    }

    @Test
    fun emptyInstagram() {
        assertEquals(false, user.hasInstagram)
        user.instagram = com.gpetuhov.android.hive.utils.Constants.DUMMY_INSTAGRAM
        assertEquals(true, user.hasInstagram)
    }

    @Test
    fun emptyYouTube() {
        assertEquals(false, user.hasYouTube)
        user.youTube = com.gpetuhov.android.hive.utils.Constants.DUMMY_YOUTUBE
        assertEquals(true, user.hasYouTube)
    }

    @Test
    fun emptyWebsite() {
        assertEquals(false, user.hasWebsite)
        user.website = com.gpetuhov.android.hive.utils.Constants.DUMMY_WEBSITE
        assertEquals(true, user.hasWebsite)
    }

    @Test
    fun emptyResidence() {
        assertEquals(false, user.hasResidence)
        user.residence = com.gpetuhov.android.hive.utils.Constants.DUMMY_RESIDENCE
        assertEquals(true, user.hasResidence)
    }

    @Test
    fun emptyLanguage() {
        assertEquals(false, user.hasLanguage)
        user.language = com.gpetuhov.android.hive.utils.Constants.DUMMY_LANGUAGE
        assertEquals(true, user.hasLanguage)
    }

    @Test
    fun emptyEducation() {
        assertEquals(false, user.hasEducation)
        user.education = com.gpetuhov.android.hive.utils.Constants.DUMMY_EDUCATION
        assertEquals(true, user.hasEducation)
    }

    @Test
    fun emptyWork() {
        assertEquals(false, user.hasWork)
        user.work = com.gpetuhov.android.hive.utils.Constants.DUMMY_WORK
        assertEquals(true, user.hasWork)
    }

    @Test
    fun emptyInterests() {
        assertEquals(false, user.hasInterests)
        user.interests = com.gpetuhov.android.hive.utils.Constants.DUMMY_INTERESTS
        assertEquals(true, user.hasInterests)
    }

    @Test
    fun emptyStatus() {
        assertEquals(false, user.hasStatus)
        user.status = com.gpetuhov.android.hive.utils.Constants.DUMMY_STATUS
        assertEquals(true, user.hasStatus)
    }

    @Test
    fun emptyActivity() {
        assertEquals(false, user.hasActivity)
        user.activity = com.gpetuhov.android.hive.utils.Constants.DUMMY_ACTIVITY
        assertEquals(true, user.hasActivity)
    }
}