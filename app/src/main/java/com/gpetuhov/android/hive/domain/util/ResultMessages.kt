package com.gpetuhov.android.hive.domain.util

interface ResultMessages {
    fun getSignOutErrorMessage(): String
    fun getSignOutNetworkErrorMessage(): String
    fun getDeleteUserSuccessMessage(): String
    fun getDeleteUserErrorMessage(): String
    fun getDeleteUserNetworkErrorMessage(): String
    fun getSaveUsernameErrorMessage(): String
    fun getSaveDescriptionErrorMessage(): String
    fun getSendMessageErrorMessage(): String
    fun getChangeUserPicErrorMessage(): String
    fun getSaveOfferErrorMessage(): String
    fun getOfferEmptyTitleErrorMessage(): String
    fun getOfferEmptyDescriptionErrorMessage(): String
    fun getOfferEmptyPhotoListErrorMessage(): String
    fun getDeleteOfferErrorMessage(): String
    fun getAddPhotoErrorMessage(): String
    fun getDeletePhotoErrorMessage(): String
    fun getAddFavoriteErrorMessage(): String
    fun getRemoveFavoriteErrorMessage(): String
    fun getReviewEmptyTextErrorMessage(): String
    fun getReviewZeroRatingErrorMessage(): String
    fun getSaveReviewErrorMessage(): String
    fun getDeleteReviewErrorMessage(): String
    fun getSaveCommentErrorMessage(): String
    fun getCommentEmptyTextErrorMessage(): String
    fun getDeleteCommentErrorMessage(): String
    fun getSavePhoneErrorMessage(): String
    fun getSaveEmailErrorMessage(): String
    fun getSaveSkypeErrorMessage(): String
    fun getSaveFacebookErrorMessage(): String
    fun getSaveTwitterErrorMessage(): String
    fun getSaveInstagramErrorMessage(): String
    fun getSaveYouTubeErrorMessage(): String
    fun getSaveWebsiteErrorMessage(): String
    fun getSaveResidenceErrorMessage(): String
    fun getSaveLanguageErrorMessage(): String
    fun getSaveEducationErrorMessage(): String
    fun getSaveWorkErrorMessage(): String
    fun getSaveInterestsErrorMessage(): String
    fun getSaveStatusErrorMessage(): String
    fun getDeleteUserPicErrorMessage(): String
}