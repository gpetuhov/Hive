package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentReviewsBinding
import com.gpetuhov.android.hive.presentation.presenter.ReviewsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment

class ReviewsFragment : BaseFragment(), ReviewsFragmentView {

    @InjectPresenter lateinit var presenter: ReviewsFragmentPresenter

//    private var controller: ReviewsListController? = null
    private var binding: FragmentReviewsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        controller = UserFavoriteListController(presenter)
//        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)

        val reviewsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.reviews_recycler_view)
//        reviewsRecyclerView?.adapter = controller?.adapter

//        val viewModel = ViewModelProviders.of(this).get(ReviewsViewModel::class.java)
//        viewModel.reviews.observe(this, Observer<MutableList<Review>> { reviewsList ->
//            binding?.reviewsListEmpty = reviewsList.isEmpty()
//            controller?.changeReviewsList(reviewsList)
//        })

        return binding?.root
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        controller?.onSaveInstanceState(outState)
//    }

}