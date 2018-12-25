package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class ReviewsFragment : BaseFragment(), ReviewsFragmentView {

    @InjectPresenter lateinit var presenter: ReviewsFragmentPresenter

    private var controller: ReviewsListController? = null
    private var binding: FragmentReviewsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
        binding?.presenter = presenter

        // TODO: remove this
        binding?.postReviewButtonVisible = true

        val args = ReviewsFragmentArgs.fromBundle(arguments)
        val offerUid = args.offerUid
        val isCurrentUser = args.isCurrentUser
        presenter.offerUid = offerUid
        presenter.isCurrentUser = isCurrentUser

        val reviewsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.reviews_recycler_view)

        // Scroll to top on every reviews list update
        controller = ReviewsListController(presenter) { reviewsRecyclerView?.scrollToPosition(0) }
        controller?.onRestoreInstanceState(savedInstanceState)

        reviewsRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(ReviewsViewModel::class.java)
        viewModel.reviews.observe(this, Observer<MutableList<Review>> { reviewsList ->
            binding?.reviewsListEmpty = reviewsList.isEmpty()
            controller?.changeReviewsList(reviewsList)
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

    // === ReviewsFragmentView ===

    override fun postReview(offerUid: String) {
        val action = ReviewsFragmentDirections.actionReviewsFragmentToUpdateReviewFragment(offerUid)
        findNavController().navigate(action)
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}