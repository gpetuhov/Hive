package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_ACTIVITY
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_DESCRIPTION
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_EDUCATION
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_EMAIL
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_FACEBOOK
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_INSTAGRAM
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_INTERESTS
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_LANGUAGE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_LOCATION
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_OFFER
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_PHONE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_RESIDENCE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_SKYPE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_STATUS
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_TWITTER
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_URL
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_USERNAME
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_WEBSITE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_WORK
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_YOUTUBE
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_PHOTO
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserTest {

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
            location = DUMMY_LOCATION
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
        user.description = DUMMY_DESCRIPTION
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
        val dummyOffer = DUMMY_OFFER
        assertEquals(null, user.getOffer(dummyOffer.uid))

        user.offerList.add(dummyOffer)
        assertEquals(dummyOffer, user.getOffer(dummyOffer.uid))
        assertEquals(null, user.getOffer("98gq"))
    }

    @Test
    fun emptyPhone() {
        assertEquals(false, user.hasPhone)
        user.phone = DUMMY_PHONE
        assertEquals(true, user.hasPhone)
    }

    @Test
    fun emptyVisibleEmail() {
        assertEquals(false, user.hasVisibleEmail)
        user.visibleEmail = DUMMY_EMAIL
        assertEquals(true, user.hasVisibleEmail)
    }

    @Test
    fun emptySkype() {
        assertEquals(false, user.hasSkype)
        user.skype = DUMMY_SKYPE
        assertEquals(true, user.hasSkype)
    }

    @Test
    fun emptyFacebook() {
        assertEquals(false, user.hasFacebook)
        user.facebook = DUMMY_FACEBOOK
        assertEquals(true, user.hasFacebook)
    }

    @Test
    fun emptyTwitter() {
        assertEquals(false, user.hasTwitter)
        user.twitter = DUMMY_TWITTER
        assertEquals(true, user.hasTwitter)
    }

    @Test
    fun emptyInstagram() {
        assertEquals(false, user.hasInstagram)
        user.instagram = DUMMY_INSTAGRAM
        assertEquals(true, user.hasInstagram)
    }

    @Test
    fun emptyYouTube() {
        assertEquals(false, user.hasYouTube)
        user.youTube = DUMMY_YOUTUBE
        assertEquals(true, user.hasYouTube)
    }

    @Test
    fun emptyWebsite() {
        assertEquals(false, user.hasWebsite)
        user.website = DUMMY_WEBSITE
        assertEquals(true, user.hasWebsite)
    }

    @Test
    fun emptyResidence() {
        assertEquals(false, user.hasResidence)
        user.residence = DUMMY_RESIDENCE
        assertEquals(true, user.hasResidence)
    }

    @Test
    fun emptyLanguage() {
        assertEquals(false, user.hasLanguage)
        user.language = DUMMY_LANGUAGE
        assertEquals(true, user.hasLanguage)
    }

    @Test
    fun emptyEducation() {
        assertEquals(false, user.hasEducation)
        user.education = DUMMY_EDUCATION
        assertEquals(true, user.hasEducation)
    }

    @Test
    fun emptyWork() {
        assertEquals(false, user.hasWork)
        user.work = DUMMY_WORK
        assertEquals(true, user.hasWork)
    }

    @Test
    fun emptyInterests() {
        assertEquals(false, user.hasInterests)
        user.interests = DUMMY_INTERESTS
        assertEquals(true, user.hasInterests)
    }

    @Test
    fun emptyStatus() {
        assertEquals(false, user.hasStatus)
        user.status = DUMMY_STATUS
        assertEquals(true, user.hasStatus)
    }

    @Test
    fun emptyActivity() {
        assertEquals(false, user.hasActivity)
        user.activity = DUMMY_ACTIVITY
        assertEquals(true, user.hasActivity)
    }

    @Test
    fun hasTextMasterAward() {
        assertEquals(false, user.hasTextMasterAward)
        fillAllFields(user)
        assertEquals(true, user.hasTextMasterAward)
    }

    @Test
    fun getNewAwards() {
        user.updateAwards()
        assertEquals(true, user.newAwardsList.isEmpty())
        fillAllFields(user)
        user.updateAwards()
        assertEquals(false, user.newAwardsList.isEmpty())
        user.awardCongratulationShownList.add(Constants.Awards.TEXT_MASTER_ID)
        user.updateAwards()
        assertEquals(true, user.newAwardsList.isEmpty())
    }

    @Test
    fun hasOfferProviderAward() {
        assertEquals(false, user.hasOfferProviderAward)
        initRirstOfferPublishedTimestamp(user)
        assertEquals(true, user.hasOfferProviderAward)
    }

    @Test
    fun hasTextMasterAwardTip() {
        user.updateAwards()
        assertEquals(true, user.awardTipsList.contains(Constants.Awards.TEXT_MASTER_ID))
        fillAllFields(user)
        user.updateAwards()
        assertEquals(false, user.awardTipsList.contains(Constants.Awards.TEXT_MASTER_ID))
    }

    @Test
    fun hasOfferProviderAwardTip() {
        user.updateAwards()
        assertEquals(true, user.awardTipsList.contains(Constants.Awards.OFFER_PROVIDER_ID))
        initRirstOfferPublishedTimestamp(user)
        user.updateAwards()
        assertEquals(false, user.awardTipsList.contains(Constants.Awards.OFFER_PROVIDER_ID))
    }

    // === Private methods ===

    private fun fillAllFields(user: User) {
        user.userPicUrl = DUMMY_URL
        user.photoList.add(DUMMY_PHOTO)
        user.username = DUMMY_USERNAME
        user.description = DUMMY_DESCRIPTION
        user.phone = DUMMY_PHONE
        user.visibleEmail = DUMMY_EMAIL
        user.skype = DUMMY_SKYPE
        user.facebook = DUMMY_FACEBOOK
        user.twitter = DUMMY_TWITTER
        user.instagram = DUMMY_INSTAGRAM
        user.youTube = DUMMY_YOUTUBE
        user.website = DUMMY_WEBSITE
        user.residence = DUMMY_RESIDENCE
        user.language = DUMMY_LANGUAGE
        user.education = DUMMY_EDUCATION
        user.work = DUMMY_WORK
        user.interests = DUMMY_INTERESTS
        user.status = DUMMY_STATUS
    }

    private fun initRirstOfferPublishedTimestamp(user: User) {
        user.firstOfferPublishedTimestamp = 100
    }
}