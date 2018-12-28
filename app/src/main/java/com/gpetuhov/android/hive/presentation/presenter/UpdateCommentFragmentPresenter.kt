package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveCommentInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.presentation.view.UpdateCommentFragmentView
import javax.inject.Inject

@InjectViewState
class UpdateCommentFragmentPresenter : MvpPresenter<UpdateCommentFragmentView>(), SaveCommentInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    var offerUid = ""
    var reviewUid = ""
    var commentText = ""

    private var initialCommentText= ""

    private var saveCommentInteractor = SaveCommentInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SaveCommentInteractor.Callback ===

    override fun onSaveCommentSuccess() {
        onOperationComplete()
        navigateUp()
    }

    override fun onSaveCommentError(errorMessage: String) {
        onOperationComplete()
        viewState.showToast(errorMessage)
    }

    // === Public methods ===
    // --- Init ---

    fun init(offerUid: String, reviewUid: String, commentText: String) {
        // Do not init presenter if already initialized or changed
        if (this.commentText == "" && this.initialCommentText == "") {
            this.offerUid = offerUid
            this.reviewUid = reviewUid
            this.commentText = commentText
            this.initialCommentText = commentText
        }
    }

    // --- Save review ---

    fun saveComment() {
        val shouldQuit = commentText != "" && !editStarted()

        if (shouldQuit) {
            // If comment text is not empty and didn't change, just quit
            navigateUp()

        } else {
            // Otherwise try to save comment
            // (if comment text is empty, show error in interactor's callback).
            viewState.disableButtons()
            viewState.showProgress()
            saveCommentInteractor.saveComment(reviewUid, offerUid, commentText)
        }
    }

    // --- Quit comment update

    fun showQuitCommentUpdateDialog() {
        if (editStarted()) {
            viewState.showQuitCommentUpdateDialog()
        } else {
            navigateUp()
        }
    }

    fun quitCommentUpdate() {
        viewState.dismissQuitCommentUpdateDialog()
        navigateUp()
    }

    fun quitCommentUpdateCancel() = viewState.dismissQuitCommentUpdateDialog()

    // === Private methods ===

    private fun navigateUp() = viewState.navigateUp()

    private fun onOperationComplete() {
        viewState.enableButtons()
        viewState.hideProgress()
    }

    private fun editStarted() = commentText != initialCommentText
}