package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveCommentInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveCommentSuccess()
        fun onSaveCommentError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var reviewUid = ""
    private var offerUid = ""
    private var commentText = ""
    private var checkValidity = true

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        if (checkValidity && commentText.trim { it <= ' ' }.isEmpty()) {
            callback.onSaveCommentError(resultMessages.getCommentEmptyTextErrorMessage())

        } else {
            repo.saveComment(
                reviewUid,
                offerUid,
                commentText,
                { callback.onSaveCommentSuccess() },
                { callback.onSaveCommentError(resultMessages.getSaveCommentErrorMessage()) }
            )
        }
    }

    // Call this method to save comment
    fun saveComment(reviewUid: String, offerUid: String, commentText: String, checkValidity: Boolean) {
        this.reviewUid = reviewUid
        this.offerUid = offerUid
        this.commentText = commentText
        this.checkValidity = checkValidity
        execute()
    }
}