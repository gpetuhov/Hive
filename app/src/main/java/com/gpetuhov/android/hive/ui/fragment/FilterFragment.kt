package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
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

    private var clearFilterDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        initDialogs()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)
        binding?.presenter = presenter

        controller = FilterListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        val filterRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.filter_recycler_view)
        filterRecyclerView?.adapter = controller?.adapter

        presenter.init()

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

    override fun onDestroyView() {
        super.onDestroyView()

        // This is needed to prevent memory leaks.
        // Here we intentially dismiss dialogs directly, not via the presenter,
        // so that Moxy command queue doesn't change.
        dismissDialogs()
    }

    // === PrivacyPolicyFragmentView ===

    override fun showClearFilterDialog() = clearFilterDialog?.show() ?: Unit

    override fun dismissClearFilterDialog() = clearFilterDialog?.dismiss() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    // === Private methods ===

    private fun initDialogs() = initClearFilterDialog()

    private fun initClearFilterDialog() {
        if (context != null) {
            clearFilterDialog = MaterialDialog(context!!)
                .title(R.string.clear_filter)
                .message(R.string.clear_filter_prompt)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton(R.string.yes) { presenter.clearFilter() }
                .negativeButton(R.string.no) { presenter.dismissClearFilterDialog() }
        }
    }

    private fun dismissDialogs() = dismissClearFilterDialog()
}