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
import com.gpetuhov.android.hive.databinding.FragmentReviewsAllBinding
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.presentation.presenter.ReviewsAllFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView
import com.gpetuhov.android.hive.ui.epoxy.review.controller.ReviewsAllListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.ReviewsAllViewModel
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

// Shows reviews on all active offers of the user
class ReviewsAllFragment : BaseFragment(), ReviewsAllFragmentView {

    @InjectPresenter lateinit var presenter: ReviewsAllFragmentPresenter

    private var controller: ReviewsAllListController? = null
    private var binding: FragmentReviewsAllBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()
        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews_all, container, false)
        binding?.presenter = presenter

        val reviewsAllRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.reviews_all_recycler_view)
        controller = ReviewsAllListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)
        reviewsAllRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(ReviewsAllViewModel::class.java)
        // TODO: get isCurrentUser from the args
        viewModel.getAllReviews(false)
        presenter.changeRating(viewModel.user)
        viewModel.allReviews.observe(this, Observer<MutableList<Review>> { reviewsList ->
            presenter.changeReviewsList(reviewsList)
        })

        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === ReviewsAllFragmentView ===

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}