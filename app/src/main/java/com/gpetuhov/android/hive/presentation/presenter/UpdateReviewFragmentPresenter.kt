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

    private var initialReviewText= ""
    private var initialRating = 0.0F

    private var saveReviewInteractor = SaveReviewInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SaveReviewInteractor.Callback ===

    override fun onSaveReviewSuccess() {
        viewState.enableButtons()
        viewState.hideProgress()
        navigateUp()
    }

    override fun onSaveReviewError(errorMessage: String) {
        viewState.enableButtons()
        viewState.hideProgress()
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
        }
    }

    // --- Save review ---

    fun saveReview() {
        viewState.disableButtons()
        viewState.showProgress()

        saveReviewInteractor.saveReview(reviewUid, offerUid, reviewText, rating)
    }

    // --- Quit review update

    fun showQuitReviewUpdateDialog() {
        val editStarted = reviewText != initialReviewText || rating != initialRating

        if (editStarted) {
            viewState.showQuitReviewUpdateDialog()
        } else {
            navigateUp()
        }
    }

    fun quitReviewUpdate() {
        viewState.dismissQuitReviewUpdateDialog()
        navigateUp()
    }

    fun quitReviewUpdateCancel() = viewState.dismissQuitReviewUpdateDialog()

    // === Private methods ===

    private fun navigateUp() = viewState.navigateUp()
}