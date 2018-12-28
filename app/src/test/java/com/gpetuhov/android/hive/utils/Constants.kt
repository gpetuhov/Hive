package com.gpetuhov.android.hive.utils

import com.gpetuhov.android.hive.domain.model.Favorite
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.Review

class Constants {
    companion object {
        const val DELETE_USER_SUCCESS = "Delete user success"
        const val DELETE_USER_ERROR = "Delete user error"
        const val DELETE_USER_NETWORK_ERROR = "Delete user network error"

        const val SIGN_OUT_ERROR = "Sign out error"
        const val SIGN_OUT_NETWORK_ERROR = "Sign out network error"

        const val DUMMY_USERNAME = "DummyUsername"
        const val SAVE_USERNAME_ERROR = "Save username error"

        const val DUMMY_DESCRIPTION = "Dummy description"
        const val SAVE_DESCRIPTION_ERROR = "Save description error"

        const val DUMMY_MESSAGE_TEXT = "Dummy message text"
        const val SEND_MESSAGE_ERROR = "Send message error"

        val DUMMY_OFFER = getDummyOffer()

        const val SAVE_OFFER_ERROR = "Save offer error"
        const val SAVE_OFFER_TITLE_ERROR = "Save offer title error"
        const val SAVE_OFFER_DESCRIPTION_ERROR = "Save offer description error"
        const val SAVE_OFFER_PHOTO_ERROR = "Save offer photo error"

        const val DELETE_OFFER_ERROR = "Delete offer error"

        val DUMMY_PHOTO get() = Photo("3984fq9f", "389gj349fj")

        const val DELETE_PHOTO_ERROR = "Delete photo error"

        const val ADD_FAVORITE_ERROR = "Add favorite error"
        const val REMOVE_FAVORITE_ERROR = "Remove favorite error"

        val DUMMY_FAVORITE get() = Favorite("8934g2", "")

        const val SAVE_REVIEW_ERROR = "Save review error"
        const val SAVE_REVIEW_TEXT_ERROR = "Save review text error"
        const val SAVE_REVIEW_RATING_ERROR = "Save review rating error"

        const val DELETE_REVIEW_ERROR = "Delete review error"

        val DUMMY_REVIEW = getDummyReview()

        const val SAVE_COMMENT_ERROR = "Save comment error"
        const val SAVE_COMMENT_TEXT_ERROR = "Save comment text error"

        const val DELETE_COMMENT_ERROR = "Delete comment error"

        private fun getDummyOffer(): Offer {
            val offer = Offer("2g5g2g", "5894gh5489","dfgfdg", "ddsfdfs", 0.0, false, true)
            offer.photoList.add(DUMMY_PHOTO)
            return offer
        }

        private fun getDummyReview() =
            Review(
                "2g5434g34",
                "25g54g",
                "g254g54g54",
                "489gh394g",
                "Uncle Bob",
                "",
                "oioigjo9i",
                4F,
                0,
                false,
                "h3h563h56"
            )
    }
}