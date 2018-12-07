package com.gpetuhov.android.hive.utils

import com.gpetuhov.android.hive.domain.model.Image
import com.gpetuhov.android.hive.domain.model.Offer

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

        val DUMMY_OFFER = Offer("2g5g2g", "dfgfdg", "ddsfdfs", 0.0, false, true)

        const val SAVE_OFFER_ERROR = "Save offer error"
        const val SAVE_OFFER_TITLE_ERROR = "Save offer title error"
        const val SAVE_OFFER_DESCRIPTION_ERROR = "Save offer description error"

        const val DELETE_OFFER_ERROR = "Delete offer error"

        val DUMMY_IMAGE = Image("3984fq9f", "389gj349fj")

        const val DELETE_PHOTO_ERROR = "Delete photo error"
    }
}