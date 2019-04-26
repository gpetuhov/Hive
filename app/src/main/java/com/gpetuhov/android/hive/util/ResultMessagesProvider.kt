package com.gpetuhov.android.hive.util

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class ResultMessagesProvider : ResultMessages {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun getSignOutErrorMessage(): String = getString(R.string.sign_out_error)

    override fun getSignOutNetworkErrorMessage(): String = getString(R.string.sign_out_no_network)

    override fun getDeleteUserSuccessMessage(): String = getString(R.string.delete_account_success)

    override fun getDeleteUserErrorMessage(): String = getString(R.string.delete_account_error)

    override fun getDeleteUserNetworkErrorMessage(): String = getString(R.string.delete_account_no_network)

    override fun getSaveUsernameErrorMessage(): String = getString(R.string.username_save_error)

    override fun getSaveDescriptionErrorMessage(): String = getString(R.string.description_save_error)

    override fun getSendMessageErrorMessage(): String = getString(R.string.send_message_error)

    override fun getChangeUserPicErrorMessage(): String = getString(R.string.change_user_picture_error)

    override fun getSaveOfferErrorMessage(): String = getString(R.string.save_offer_error)

    override fun getOfferEmptyTitleErrorMessage(): String = getString(R.string.title_empty_error)

    override fun getOfferEmptyDescriptionErrorMessage(): String = getString(R.string.description_empty_error)

    override fun getOfferEmptyPhotoListErrorMessage(): String = getString(R.string.photo_list_empty_error)

    override fun getDeleteOfferErrorMessage(): String = getString(R.string.delete_offer_error)

    override fun getAddPhotoErrorMessage(): String = getString(R.string.add_photo_error)

    override fun getDeletePhotoErrorMessage(): String = getString(R.string.delete_photo_error)

    override fun getAddFavoriteErrorMessage(): String = getString(R.string.add_favorite_error)

    override fun getRemoveFavoriteErrorMessage(): String = getString(R.string.remove_favorite_error)

    override fun getReviewEmptyTextErrorMessage(): String = getString(R.string.review_text_empty_error)

    override fun getReviewZeroRatingErrorMessage(): String = getString(R.string.review_zero_rating_error)

    override fun getSaveReviewErrorMessage(): String = getString(R.string.save_review_error)

    override fun getDeleteReviewErrorMessage(): String = getString(R.string.delete_review_error)

    override fun getSaveCommentErrorMessage(): String = getString(R.string.save_comment_error)

    override fun getCommentEmptyTextErrorMessage(): String = getString(R.string.comment_text_empty_error)

    override fun getDeleteCommentErrorMessage(): String = getString(R.string.delete_comment_error)

    override fun getSavePhoneErrorMessage(): String = getString(R.string.phone_save_error)

    override fun getSaveEmailErrorMessage(): String = getString(R.string.email_save_error)

    override fun getSaveSkypeErrorMessage(): String = getString(R.string.skype_save_error)

    override fun getSaveFacebookErrorMessage(): String = getString(R.string.facebook_save_error)

    private fun getString(stringId: Int) = context.getString(stringId)
}