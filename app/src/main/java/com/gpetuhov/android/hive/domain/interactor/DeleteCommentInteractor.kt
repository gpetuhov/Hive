package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteCommentInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteCommentSuccess()
        fun onDeleteCommentError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var reviewUid = ""
    private var offerUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        // To delete comment we just save it with empty text
        repo.saveComment(
            reviewUid,
            offerUid,
            "",
            { callback.onDeleteCommentSuccess() },
            { callback.onDeleteCommentError(resultMessages.getDeleteCommentErrorMessage()) }
        )
    }

    // Call this method to delete comment
    fun deleteComment(reviewUid: String, offerUid: String) {
        this.reviewUid = reviewUid
        this.offerUid = offerUid
        execute()
    }
}