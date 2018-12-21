package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class UpdateReviewFragment : BaseFragment(), UpdateReviewFragmentView {

    @InjectPresenter lateinit var presenter: UpdateReviewFragmentPresenter

    //    private var controller: ReviewsListController? = null
//    private var binding: FragmentReviewsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

//        controller = UserFavoriteListController(presenter)
//        controller?.onRestoreInstanceState(savedInstanceState)

//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
//        binding?.presenter = presenter
//
//        val reviewsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.reviews_recycler_view)
//        reviewsRecyclerView?.adapter = controller?.adapter

//        val viewModel = ViewModelProviders.of(this).get(ReviewsViewModel::class.java)
//        viewModel.reviews.observe(this, Observer<MutableList<Review>> { reviewsList ->
//            binding?.reviewsListEmpty = reviewsList.isEmpty()
//            controller?.changeReviewsList(reviewsList)
//        })

//        return binding?.root

        return null
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        controller?.onSaveInstanceState(outState)
//    }

    // === ReviewsFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}