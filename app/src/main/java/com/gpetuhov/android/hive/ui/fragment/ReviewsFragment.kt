package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentReviewsBinding
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.presentation.presenter.ReviewsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView
import com.gpetuhov.android.hive.ui.epoxy.review.controller.ReviewsListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.ReviewsViewModel
import com.gpetuhov.android.hive.util.hideBottomNavigationView
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class ReviewsFragment : BaseFragment(), ReviewsFragmentView {

    @InjectPresenter lateinit var presenter: ReviewsFragmentPresenter

    private var controller: ReviewsListController? = null
    private var binding: FragmentReviewsBinding? = null
    private var deleteReviewDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()
        hideToolbar()

        initDialogs()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
        binding?.presenter = presenter

        val args = ReviewsFragmentArgs.fromBundle(arguments)
        val offerUid = args.offerUid
        val isCurrentUser = args.isCurrentUser
        presenter.offerUid = offerUid
        presenter.isCurrentUser = isCurrentUser

        // Hide bottom nav view for current user
        if (isCurrentUser) hideBottomNavigationView() else showBottomNavigationView()

        val reviewsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.reviews_recycler_view)

        // Scroll to top on every reviews list update
        controller = ReviewsListController(presenter) { reviewsRecyclerView?.scrollToPosition(0) }
        controller?.onRestoreInstanceState(savedInstanceState)

        reviewsRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(ReviewsViewModel::class.java)
        viewModel.reviews.observe(this, Observer<MutableList<Review>> { reviewsList ->
            binding?.reviewsListEmpty = reviewsList.isEmpty()
            presenter.changeReviewsList(reviewsList)
        })

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialogs()
    }

    // === ReviewsFragmentView ===

    override fun showDeleteReviewDialog() = deleteReviewDialog?.show() ?: Unit

    override fun dismissDeleteReviewDialog() = deleteReviewDialog?.dismiss() ?: Unit

    override fun updateUI() {
        binding?.postReviewButtonVisible = presenter.postReviewEnabled
        controller?.requestModelBuild() ?: Unit
    }

    override fun postReview(offerUid: String) {
        val action = ReviewsFragmentDirections.actionReviewsFragmentToUpdateReviewFragment(offerUid)
        findNavController().navigate(action)
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun initDialogs() {
        initDeleteReviewDialog()
    }

    private fun initDeleteReviewDialog() {
        if (context != null) {
            deleteReviewDialog = MaterialDialog(context!!)
                .title(R.string.delete_review)
                .message(R.string.prompt_delete_review)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deleteReview() }
                .negativeButton { presenter.deleteReviewCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissDeleteReviewDialog()
    }
}