package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveReviewInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveReviewSuccess()
        fun onSaveReviewError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var reviewUid = ""
    private var offerUid = ""
    private var reviewText = ""
    private var rating = 0.0F

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        when {
            reviewText.trim { it <= ' ' }.isEmpty() -> callback.onSaveReviewError(resultMessages.getReviewEmptyTextErrorMessage())
            rating == 0.0F -> callback.onSaveReviewError(resultMessages.getReviewZeroRatingErrorMessage())
            else -> repo.saveReview(
                reviewUid,
                offerUid,
                reviewText,
                rating,
                { callback.onSaveReviewSuccess() },
                { callback.onSaveReviewError(resultMessages.getSaveReviewErrorMessage()) }
            )
        }
    }

    // Call this method to save review
    fun saveReview(reviewUid: String, offerUid: String, reviewText: String, rating: Float) {
        this.reviewUid = reviewUid
        this.offerUid = offerUid
        this.reviewText = reviewText
        this.rating = rating
        execute()
    }
}