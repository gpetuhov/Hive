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
import com.gpetuhov.android.hive.databinding.FragmentUpdateReviewBinding
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import com.gpetuhov.android.hive.ui.epoxy.review.update.controller.UpdateReviewListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class UpdateReviewFragment : BaseFragment(), UpdateReviewFragmentView {

    @InjectPresenter lateinit var presenter: UpdateReviewFragmentPresenter

    private var controller: UpdateReviewListController? = null
    private var binding: FragmentUpdateReviewBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        controller = UpdateReviewListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_review, container, false)
        binding?.presenter = presenter

        val updateReviewRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.update_review_recycler_view)
        updateReviewRecyclerView?.adapter = controller?.adapter

        updateUI()

        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === UpdateReviewFragmentView ===

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}