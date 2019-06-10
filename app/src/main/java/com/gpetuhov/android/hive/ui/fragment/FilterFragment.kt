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
import com.gpetuhov.android.hive.databinding.FragmentFilterBinding
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.FilterFragmentView
import com.gpetuhov.android.hive.ui.epoxy.filter.controller.FilterListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.*

class FilterFragment : BaseFragment(), FilterFragmentView {

    @InjectPresenter lateinit var presenter: FilterFragmentPresenter

    private var controller: FilterListController? = null
    private var binding: FragmentFilterBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)
        binding?.presenter = presenter

        controller = FilterListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        val filterRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.filter_recycler_view)
        filterRecyclerView?.adapter = controller?.adapter

        updateUI()

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        controller?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    // === PrivacyPolicyFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun updateUI() = controller?.requestModelBuild()
}