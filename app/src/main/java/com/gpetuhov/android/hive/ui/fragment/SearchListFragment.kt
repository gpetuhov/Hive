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
import com.gpetuhov.android.hive.databinding.FragmentSearchListBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.SearchListFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.SearchListFragmentView
import com.gpetuhov.android.hive.ui.epoxy.search.controller.SearchListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.gpetuhov.android.hive.util.*
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_search_list.*

class SearchListFragment : BaseFragment(), SearchListFragmentView {

    @InjectPresenter lateinit var presenter: SearchListFragmentPresenter

    private var binding: FragmentSearchListBinding? = null
    private var controller: SearchListController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        controller = SearchListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false)
        binding?.presenter = presenter

        val searchListRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.search_list_recycler_view)
        searchListRecyclerView?.adapter = controller?.adapter

        val args = SearchListFragmentArgs.fromBundle(arguments!!)
        presenter.queryLatitude = args.queryLatitude.toDouble()
        presenter.queryLongitude = args.queryLongitude.toDouble()
        presenter.queryRadius = args.queryRadius.toDouble()

        presenter.initSearchQueryText()

        val viewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        viewModel.searchResult.observe(this, Observer<MutableMap<String, User>> { searchResult ->
            presenter.updateSearchResult(searchResult)
        })

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        hideSoftKeyboard()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        controller?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    // === SearchListFragmentView ===

    override fun onSearchStart() = progressVisible(true)

    override fun onSearchComplete() = progressVisible(false)

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    override fun showDetails(offerUid: String) {
        val action = if(offerUid != "") SearchListFragmentDirections.actionSearchListFragmentToOfferDetailsFragment(offerUid)
        else SearchListFragmentDirections.actionSearchListFragmentToUserDetailsFragment()

        findNavController().navigate(action)
    }

    override fun showSearchDialog() {
        // TODO
    }

    override fun dismissSearchDialog() {
        // TODO
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

    private fun progressVisible(isVisible: Boolean) = search_list_progress.setVisible(isVisible)
}