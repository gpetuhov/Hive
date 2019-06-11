package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.utils.Constants.Companion.DUMMY_OFFER
import org.junit.Assert.*
import org.junit.Test

class OfferTest {

    @Test
    fun hasReviews() {
        val offer = DUMMY_OFFER.copy()
        assertEquals(false, offer.hasReviews)
        offer.reviewCount = 10
        assertEquals(true, offer.hasReviews)
    }
}