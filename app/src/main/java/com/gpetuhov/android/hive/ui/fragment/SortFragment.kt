package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentSortBinding
import com.gpetuhov.android.hive.presentation.presenter.SortFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.SortFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideBottomNavigationView
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan

class SortFragment : BaseFragment(), SortFragmentView {

    @InjectPresenter lateinit var presenter: SortFragmentPresenter

    private var binding: FragmentSortBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    // === SortFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}