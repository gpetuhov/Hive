package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteReviewInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteReviewSuccess()
        fun onDeleteReviewError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var offerUid = ""
    private var reviewUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.deleteReview(
            offerUid,
            reviewUid,
            { callback.onDeleteReviewSuccess() },
            { callback.onDeleteReviewError(resultMessages.getDeleteReviewErrorMessage()) }
        )
    }

    // Call this method to delete review
    fun deleteReview(offerUid: String, reviewUid: String) {
        this.offerUid = offerUid
        this.reviewUid = reviewUid
        execute()
    }
}