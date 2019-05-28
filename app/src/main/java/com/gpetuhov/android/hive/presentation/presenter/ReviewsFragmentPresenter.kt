package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteCommentInteractor
import com.gpetuhov.android.hive.domain.interactor.DeleteReviewInteractor
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView
import javax.inject.Inject

@InjectViewState
class ReviewsFragmentPresenter :
    MvpPresenter<ReviewsFragmentView>(),
    DeleteReviewInteractor.Callback,
    DeleteCommentInteractor.Callback {

    @Inject lateinit var repo: Repo

    var userUid = ""
    var offerUid = ""
    var isCurrentUser = false
    var reviewsList = mutableListOf<Review>()
    var reviewCount = 0
    var rating = 0.0F
    var secondUser: User? = null
    var postReviewEnabled = true
    var isReviewListVisible = false
    var isNoReviewsVisible = false
    var isOfferDeletedVisible = false
    var isUserDeletedVisible = false

    private var deleteReviewUid = ""
    private var deleteCommentReviewUid = ""
    private var isReviewListEmpty = false
    private var isFirstReview = false

    private val deleteReviewInteractor = DeleteReviewInteractor(this)
    private var deleteCommentInteractor = DeleteCommentInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === DeleteReviewInteractor.Callback ===

    override fun onDeleteReviewSuccess() {
        // Do nothing
    }

    override fun onDeleteReviewError(errorMessage: String) = viewState.showToast(errorMessage)

    // === DeleteCommentInteractor.Callback ===

    override fun onDeleteCommentSuccess() {
        // Do nothing
    }

    override fun onDeleteCommentError(errorMessage: String) =  viewState.showToast(errorMessage)

    // === Public methods ===

    // --- Init ---

    fun changeReviewsList(reviewsList: MutableList<Review>) {
        this.reviewsList = reviewsList
        this.reviewCount = reviewsList.size
        this.isReviewListEmpty = reviewsList.isEmpty()
        this.isReviewListVisible = !isReviewListEmpty
    }

    fun updateReviews() {
        postReviewEnabled = true
        var reviewFromCurrentUserExist = false

        if (reviewCount == 0) {
            rating = 0.0F
        } else {
            var ratingSum = 0.0F

            reviewsList.forEach {
                ratingSum += it.rating

                if (it.authorUid == repo.currentUserUid()) {
                    reviewFromCurrentUserExist = true
                }
            }

            rating = ratingSum / reviewCount
        }

        // Show user and offer deleted messages for not current user only
        val isUserDeleted = !isCurrentUser && (secondUser?.isDeleted == true)
        val isOfferDeleted = !isCurrentUser && ((secondUser?.offerList?.firstOrNull { it.uid == offerUid }) == null)

        // Hide post review button if current offer belongs to current user
        // or if current user has already posted review on the current offer
        // or we are in second user and second user is deleted or offer is deleted.
        postReviewEnabled = !(isCurrentUser || reviewFromCurrentUserExist || isUserDeleted || isOfferDeleted)

        isNoReviewsVisible = isReviewListEmpty && !isUserDeleted && !isOfferDeleted
        isUserDeletedVisible = isReviewListEmpty && isUserDeleted
        isOfferDeletedVisible = isReviewListEmpty && !isUserDeleted && isOfferDeleted

        viewState.updateUI()
    }

    // --- Edit review ---

    fun editReview(reviewUid: String, reviewText: String, rating: Float) =
        viewState.editReview(offerUid, reviewUid, reviewText, rating)

    // --- Delete review ---

    fun showDeleteReviewDialog(reviewUid: String, isFirstReview: Boolean) {
        deleteReviewUid = reviewUid
        this.isFirstReview = isFirstReview
        viewState.showDeleteReviewDialog()
    }

    fun deleteReview() {
        viewState.dismissDeleteReviewDialog()
        deleteReviewInteractor.deleteReview(offerUid, deleteReviewUid, isFirstReview)
    }

    fun deleteReviewCancel() = viewState.dismissDeleteReviewDialog()

    // --- Post review ---

    fun postReview() = viewState.postReview(offerUid, isReviewListEmpty)

    // --- Edit comment ---

    fun editComment(reviewUid: String, commentText: String) = viewState.editComment(offerUid, reviewUid, commentText)

    // --- Delete comment ---

    fun showDeleteCommentDialog(reviewUid: String) {
        deleteCommentReviewUid = reviewUid
        viewState.showDeleteCommentDialog()
    }

    fun deleteComment() {
        viewState.dismissDeleteCommentDialog()
        deleteCommentInteractor.deleteComment(deleteCommentReviewUid, offerUid)
    }

    fun deleteCommentCancel() = viewState.dismissDeleteCommentDialog()

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onResume() {
        repo.startGettingReviewsUpdates(offerUid, isCurrentUser)
        if (!isCurrentUser) repo.startGettingSecondUserReviewsUpdates(userUid)
    }

    fun onPause() {
        repo.stopGettingReviewsUpdates()
        if (!isCurrentUser) repo.stopGettingSecondUserReviewsUpdates()
    }
}