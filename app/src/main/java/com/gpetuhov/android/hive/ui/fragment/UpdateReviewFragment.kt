package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUpdateReviewBinding
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class UpdateReviewFragment : BaseFragment(), UpdateReviewFragmentView {

    @InjectPresenter lateinit var presenter: UpdateReviewFragmentPresenter

    private var binding: FragmentUpdateReviewBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_review, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    // === UpdateReviewFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}