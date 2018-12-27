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
}