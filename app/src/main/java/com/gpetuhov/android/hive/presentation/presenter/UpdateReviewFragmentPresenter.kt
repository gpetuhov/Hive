package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import javax.inject.Inject

@InjectViewState
class UpdateReviewFragmentPresenter : MvpPresenter<UpdateReviewFragmentView>() {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    var reviewUid = ""

    // TODO: init it !!!
    var offerUid = ""

    var reviewText = ""
    var rating = 0.0F

    private var tempReviewText = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    // --- Save review ---

    fun saveReview() {
        // TODO: refactor this into interactor
        // TODO: handle on success and on error
        repo.saveReview(reviewUid, offerUid, reviewText, rating, {}, {})
    }

    // --- Quit offer update

    fun showQuitReviewUpdateDialog() {
        if (reviewText != "") {
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