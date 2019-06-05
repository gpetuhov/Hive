package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.maps.GoogleMap
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentMapBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.MapFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_map.*
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController
import com.gpetuhov.android.hive.ui.fragment.base.BaseMapFragment
import com.gpetuhov.android.hive.util.*


class MapFragment : BaseMapFragment(), MapFragmentView {

    @InjectPresenter lateinit var presenter: MapFragmentPresenter

    private var binding: FragmentMapBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding?.presenter = presenter

        val rootView = binding?.root
        initMap(rootView, R.id.map_view, savedInstanceState)
        presenter.onCreateView(savedInstanceState)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This is needed to perform search on keyboard search button click.
        // Don't forget to set android:imeOptions="actionSearch" and android:inputType="text"
        // for the corresponding EditText!
        query_text.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.search()
                true
            } else {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.search()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    // === OnMapReadyCallback ===

    override fun onMapReady(map: GoogleMap) {
        presenter.initMap(map)

        mapControlsVisible()

        // Start observing result list ViewModel only after map is ready
        val viewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        viewModel.searchResult.observe(this, Observer<MutableMap<String, User>> { searchResult ->
            presenter.updateMarkers(searchResult)
        })
    }

    // === MapFragmentView ===

    override fun onSearchStart() {
        progressVisible(true)
        buttonsEnabled(false)
    }

    override fun onSearchComplete() {
        progressVisible(false)
        buttonsEnabled(true)
    }

    override fun clearSearch() = query_text.setText("")

    override fun showToast(message: String) {
        toast(message)
    }

    override fun hideKeyboard() {
        // Remove cursor from EditText
        query_text.clearFocus()

        hideSoftKeyboard()
    }

    override fun onMinZoom() {
        zoomInEnabled(true)
        zoomOutEnabled(false)
    }

    override fun onMaxZoom() {
        zoomInEnabled(false)
        zoomOutEnabled(true)
    }

    override fun onNormalZoom() {
        zoomInEnabled(true)
        zoomOutEnabled(true)
    }

    override fun showDetails(userUid: String, offerUid: String) {
        // If offer uid not empty, open offer details.
        // Otherwise, open user details
        val action = if(offerUid != "") MapFragmentDirections.actionNavigationSearchToOfferDetailsFragment(offerUid)
        else MapFragmentDirections.actionNavigationSearchToUserDetailsFragment()

        findNavController().navigate(action)
    }

    override fun showList(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String) {
        val action = MapFragmentDirections.actionNavigationSearchToSearchListFragment(queryLatitude.toFloat(), queryLongitude.toFloat(), queryRadius.toFloat(), queryText)
        findNavController().navigate(action)
    }

    // === Private methods ===

    private fun buttonsEnabled(isEnabled: Boolean) {
        search_users_button.isEnabled = isEnabled
        cancel_search_users_button.isEnabled = isEnabled
        show_search_list_button.isEnabled = isEnabled
    }

    private fun progressVisible(isVisible: Boolean) = search_progress.setVisible(isVisible)

    private fun mapControlsVisible() {
        my_location_button.visibility = View.VISIBLE
        zoom_in_button.visibility = View.VISIBLE
        zoom_out_button.visibility = View.VISIBLE
    }

    private fun zoomInEnabled(isEnabled: Boolean) {
        zoom_in_button.isEnabled = isEnabled
    }

    private fun zoomOutEnabled(isEnabled: Boolean) {
        zoom_out_button.isEnabled = isEnabled
    }
}