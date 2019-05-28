package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveReviewInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import javax.inject.Inject

@InjectViewState
class UpdateReviewFragmentPresenter : MvpPresenter<UpdateReviewFragmentView>(), SaveReviewInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    var offerUid = ""
    var reviewUid = ""
    var reviewText = ""
    var rating = 0.0F
    var isNew = false   // true if review is new

    private var initialReviewText= ""
    private var initialRating = 0.0F

    private var saveReviewInteractor = SaveReviewInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SaveReviewInteractor.Callback ===

    override fun onSaveReviewSuccess() {
        onOperationComplete()
        navigateUp()
    }

    override fun onSaveReviewError(errorMessage: String) {
        onOperationComplete()
        viewState.showToast(errorMessage)
    }

    // === Public methods ===
    // --- Init ---

    fun init(offerUid: String, reviewUid: String, reviewText: String, rating: Float) {
        // Do not init presenter if already initialized or changed
        if (
            this.reviewText == ""
            && this.rating == 0.0F
            && this.initialReviewText == ""
            && this.initialRating == 0.0F
        ) {
            this.offerUid = offerUid
            this.reviewUid = reviewUid
            this.reviewText = reviewText
            this.rating = rating

            this.initialReviewText = reviewText
            this.initialRating = rating

            if (this.initialReviewText == "" && this.initialRating == 0.0F) isNew = true
        }
    }

    // --- Save review ---

    fun saveReview() {
        val shouldQuit = reviewText != "" && rating != 0.0F && !editStarted()

        if (shouldQuit) {
            // If review text is not empty and rating is not zero
            // and edit not started, just quit.
            navigateUp()

        } else {
            // Otherwise try to save review
            // (if review text is empty or rating is zero, show error in interactor's callback).
            viewState.disableButtons()
            viewState.showProgress()
            saveReviewInteractor.saveReview(reviewUid, offerUid, reviewText, rating, isNew)
        }
    }

    // --- Quit review update

    fun showQuitReviewUpdateDialog() {
        if (editStarted()) {
            viewState.showQuitReviewUpdateDialog()
        } else {
            navigateUp()
        }
    }

    fun quitReviewUpdate() {
        viewState.dismissQuitReviewUpdateDialog()
        navigateUp()
    }

    fun saveFromQuitDialog() {
        viewState.dismissQuitReviewUpdateDialog()
        saveReview()
    }

    fun quitReviewUpdateCancel() = viewState.dismissQuitReviewUpdateDialog()

    // === Private methods ===

    private fun navigateUp() = viewState.navigateUp()

    private fun onOperationComplete() {
        viewState.enableButtons()
        viewState.hideProgress()
    }

    private fun editStarted() = reviewText != initialReviewText || rating != initialRating
}