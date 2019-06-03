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
import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_LOCATION2
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
import java.util.*

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

    @Test
    fun hasAltruistAward() {
        user.updateAwards()
        assertEquals(false, user.hasAltruistAward)
        addAllFreeOffers(Constants.Offer.MAX_ACTIVE_OFFER_COUNT)
        user.updateAwards()
        assertEquals(true, user.hasAltruistAward)
    }

    @Test
    fun hasAltruistAwardTip() {
        user.updateAwards()
        assertEquals(true, user.awardTipsList.contains(Constants.Awards.ALTRUIST_ID))
        addAllFreeOffers(Constants.Offer.MAX_ACTIVE_OFFER_COUNT)
        user.updateAwards()
        assertEquals(false, user.awardTipsList.contains(Constants.Awards.ALTRUIST_ID))
    }

    @Test
    fun hasGoodProviderAward() {
        assertEquals(false, user.hasGoodProviderAward)
        user.totalReviewsCount = Constants.Awards.GOOD_PROVIDER_AWARD_MIN_REVIEW_COUNT
        user.averageRating = Constants.Awards.GOOD_PROVIDER_AWARD_MIN_RATING
        assertEquals(true, user.hasGoodProviderAward)
    }

    @Test
    fun hasSuperProviderAward() {
        assertEquals(false, user.hasGoodProviderAward)
        assertEquals(false, user.hasSuperProviderAward)
        user.totalReviewsCount = Constants.Awards.SUPER_PROVIDER_AWARD_MIN_REVIEW_COUNT
        user.averageRating = Constants.Awards.SUPER_PROVIDER_AWARD_MIN_RATING

        // If user has SuperProvider award, user must NOT have GoodProvider award
        assertEquals(false, user.hasGoodProviderAward)
        assertEquals(true, user.hasSuperProviderAward)
    }

    @Test
    fun hasNewbieAward() {
        assertEquals(false, user.hasNewbieAward())
        user.creationTimestamp = Date().time
        assertEquals(true, user.hasNewbieAward())
    }

    @Test
    fun hasReviewedProviderAward() {
        assertEquals(false, user.hasReviewedProviderAward)
        user.totalReviewsCount = 1
        assertEquals(true, user.hasReviewedProviderAward)
    }

    @Test
    fun hasHiveCoreAward() {
        user.updateAwards()
        assertEquals(false, user.hasHiveCoreAward)
        addAllFreeOffers(Constants.Awards.HIVECORE_AWARD_MIN_ACTIVE_OFFER_COUNT)
        user.updateAwards()
        assertEquals(true, user.hasHiveCoreAward)
    }

    @Test
    fun hasReviewPosterAward() {
        assertEquals(false, user.hasReviewPosterAward)
        user.postedReviewsCount = 1
        assertEquals(true, user.hasReviewPosterAward)
    }

    @Test
    fun hasStoryTellerAward() {
        assertEquals(false, user.hasStoryTellerAward)
        user.postedReviewsCount = Constants.Awards.STORY_TELLER_AWARD_MIN_REVIEW_COUNT
        assertEquals(true, user.hasStoryTellerAward)
    }

    @Test
    fun hasMegaCriticAward() {
        assertEquals(false, user.hasMegaCriticAward)
        user.postedReviewsCount = Constants.Awards.MEGA_CRITIC_AWARD_MIN_REVIEW_COUNT
        assertEquals(true, user.hasMegaCriticAward)
    }

    @Test
    fun hasOfferFinderAward() {
        assertEquals(false, user.hasOfferFinderAward)
        user.postedFirstReviewsCount = 1
        assertEquals(true, user.hasOfferFinderAward)
    }

    @Test
    fun hasOflumbusAward() {
        assertEquals(false, user.hasOflumbusAward)
        user.postedFirstReviewsCount = Constants.Awards.OFLUMBUS_AWARD_MIN_REVIEW_COUNT
        assertEquals(true, user.hasOflumbusAward)
    }

    @Test
    fun totalStarCount() {
        user.updateAwards()
        assertEquals(0, user.totalStarCount)
        user.userStarCount = 1
        user.updateAwards()
        assertEquals(1, user.totalStarCount)
        addAllFreeOffers(9)
        user.updateAwards()
        assertEquals(10, user.totalStarCount)
    }

    @Test
    fun hasFavoriteProviderAward() {
        assertEquals(false, user.hasFavoriteProviderAward)
        user.totalStarCount = 1
        assertEquals(true, user.hasFavoriteProviderAward)
    }

    @Test
    fun hasAdorableProviderAward() {
        assertEquals(false, user.hasAdorableProviderAward)
        user.totalStarCount = Constants.Awards.ADORABLE_PROVIDER_AWARD_MIN_STAR_COUNT
        assertEquals(true, user.hasAdorableProviderAward)
    }

    @Test
    fun hasRockStarAward() {
        assertEquals(false, user.hasRockStarAward)
        user.totalStarCount = Constants.Awards.ROCK_STAR_AWARD_MIN_STAR_COUNT
        assertEquals(true, user.hasRockStarAward)
    }

    @Test
    fun isVisibleInfoChanged() {
        var userOld = user.copy()
        var userNew = user.copy()
        assertEquals(false, userOld.isVisibleInfoChanged(userNew))
        userNew.username = "username"
        assertEquals(true, userOld.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userNew = user.copy()
        userNew.isHiveRunning = true
        assertEquals(true, userOld.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userNew = user.copy()
        userNew.offerSearchResultIndex = 0
        assertEquals(true, userOld.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userNew = user.copy()
        userNew.location = DUMMY_LOCATION2
        assertEquals(true, userOld.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userOld.offerList.add(DUMMY_OFFER)
        userOld.offerSearchResultIndex = 0
        userNew = userOld.copy()
        userNew.offerList.add(DUMMY_OFFER.copy())
        assertEquals(false, userOld.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userOld.offerList.add(DUMMY_OFFER)
        userOld.offerSearchResultIndex = 0
        userNew = user.copy()
        var offerNew = DUMMY_OFFER.copy()
        offerNew.title = "title"
        userNew.offerList.add(offerNew)
        userNew.offerSearchResultIndex = 0
        assertEquals(true, user.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userOld.offerList.add(DUMMY_OFFER)
        userOld.offerSearchResultIndex = 0
        userNew = user.copy()
        offerNew = DUMMY_OFFER.copy()
        offerNew.isFree = true
        userNew.offerList.add(offerNew)
        userNew.offerSearchResultIndex = 0
        assertEquals(true, user.isVisibleInfoChanged(userNew))

        userOld = user.copy()
        userOld.offerList.add(DUMMY_OFFER)
        userOld.offerSearchResultIndex = 0
        userNew = user.copy()
        offerNew = DUMMY_OFFER.copy()
        offerNew.price = 5.0
        userNew.offerList.add(offerNew)
        userNew.offerSearchResultIndex = 0
        assertEquals(true, user.isVisibleInfoChanged(userNew))
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

    private fun addAllFreeOffers(offerCount: Int) {
        repeat(offerCount) {
            val offer = DUMMY_OFFER
            offer.isFree = true
            user.offerList.add(DUMMY_OFFER)
        }
    }
}