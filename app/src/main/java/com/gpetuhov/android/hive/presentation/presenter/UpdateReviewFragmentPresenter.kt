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

    var reviewUid = ""
    var offerUid = ""
    var reviewText = ""
    var rating = 0.0F

    private var tempReviewText = ""

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

    // --- Save review ---

    fun saveReview() {
        viewState.disableButtons()
        viewState.showProgress()

        saveReviewInteractor.saveOffer(reviewUid, offerUid, reviewText, rating)
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