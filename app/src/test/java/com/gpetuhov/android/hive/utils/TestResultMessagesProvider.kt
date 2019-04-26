package com.gpetuhov.android.hive.utils

import com.gpetuhov.android.hive.domain.util.ResultMessages

class TestResultMessagesProvider : ResultMessages {
    override fun getSignOutErrorMessage() = Constants.SIGN_OUT_ERROR

    override fun getSignOutNetworkErrorMessage() = Constants.SIGN_OUT_NETWORK_ERROR

    override fun getDeleteUserSuccessMessage() = Constants.DELETE_USER_SUCCESS

    override fun getDeleteUserErrorMessage() = Constants.DELETE_USER_ERROR

    override fun getDeleteUserNetworkErrorMessage() = Constants.DELETE_USER_NETWORK_ERROR

    override fun getSaveUsernameErrorMessage() = Constants.SAVE_USERNAME_ERROR

    override fun getSaveDescriptionErrorMessage() = Constants.SAVE_DESCRIPTION_ERROR

    override fun getSendMessageErrorMessage(): String = Constants.SEND_MESSAGE_ERROR

    override fun getSaveOfferErrorMessage(): String = Constants.SAVE_OFFER_ERROR

    override fun getOfferEmptyTitleErrorMessage(): String = Constants.SAVE_OFFER_TITLE_ERROR

    override fun getOfferEmptyDescriptionErrorMessage(): String = Constants.SAVE_OFFER_DESCRIPTION_ERROR

    override fun getOfferEmptyPhotoListErrorMessage(): String = Constants.SAVE_OFFER_PHOTO_ERROR

    override fun getDeleteOfferErrorMessage(): String = Constants.DELETE_OFFER_ERROR

    override fun getAddPhotoErrorMessage(): String = ""

    override fun getDeletePhotoErrorMessage(): String = Constants.DELETE_PHOTO_ERROR

    override fun getChangeUserPicErrorMessage(): String = ""

    override fun getAddFavoriteErrorMessage(): String = Constants.ADD_FAVORITE_ERROR

    override fun getRemoveFavoriteErrorMessage(): String = Constants.REMOVE_FAVORITE_ERROR

    override fun getReviewEmptyTextErrorMessage(): String = Constants.SAVE_REVIEW_TEXT_ERROR

    override fun getReviewZeroRatingErrorMessage(): String = Constants.SAVE_REVIEW_RATING_ERROR

    override fun getSaveReviewErrorMessage(): String = Constants.SAVE_REVIEW_ERROR

    override fun getDeleteReviewErrorMessage(): String = Constants.DELETE_REVIEW_ERROR

    override fun getSaveCommentErrorMessage(): String = Constants.SAVE_COMMENT_ERROR

    override fun getCommentEmptyTextErrorMessage(): String = Constants.SAVE_COMMENT_TEXT_ERROR

    override fun getDeleteCommentErrorMessage(): String = Constants.DELETE_COMMENT_ERROR

    override fun getSavePhoneErrorMessage(): String = Constants.SAVE_PHONE_ERROR

    override fun getSaveEmailErrorMessage(): String = Constants.SAVE_EMAIL_ERROR

    override fun getSaveSkypeErrorMessage(): String = Constants.SAVE_SKYPE_ERROR

    override fun getSaveFacebookErrorMessage(): String = Constants.SAVE_FACEBOOK_ERROR
}