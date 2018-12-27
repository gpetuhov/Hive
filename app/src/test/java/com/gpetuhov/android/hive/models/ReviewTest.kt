package com.gpetuhov.android.hive.models

import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.utils.Constants
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ReviewTest {

    private lateinit var review: Review

    @Before
    fun initReview() {
        review = Constants.DUMMY_REVIEW
    }

    @Test
    fun hasComment() {
        assertEquals(false, review.hasComment())
        review.comment = "5489g28954gh"
        assertEquals(true, review.hasComment())
    }
}