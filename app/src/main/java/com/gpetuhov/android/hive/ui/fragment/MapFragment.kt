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
import com.google.android.gms.maps.MapView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentMapBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.MapFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_map.*
import android.view.inputmethod.EditorInfo
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController


class MapFragment : MvpAppCompatFragment(), MapFragmentView {

    @InjectPresenter lateinit var presenter: MapFragmentPresenter

    private var mapView: MapView? = null
    private var binding: FragmentMapBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding?.presenter = presenter

        val view = binding?.root
        mapView = view?.findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)

        presenter.onCreateView(savedInstanceState)

        // Asynchronously get reference to the map
        mapView?.getMapAsync(this::onMapReady)

        return view
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

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        presenter.search()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
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

        // Hide keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
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

    override fun showDetails(uid: String) {
        val action = MapFragmentDirections.actionNavigationSearchToDetailsFragment()
        findNavController().navigate(action)
    }

    // === Private methods ===

    private fun onMapReady(map: GoogleMap) {
        presenter.initMap(map)

        mapControlsVisible()

        // Start observing result list ViewModel only after map is ready
        val viewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        viewModel.searchResult.observe(this, Observer<MutableMap<String, User>> { searchResult ->
            presenter.updateMarkers(searchResult)
        })
    }

    private fun buttonsEnabled(isEnabled: Boolean) {
        search_users_button.isEnabled = isEnabled
        cancel_search_users_button.isEnabled = isEnabled
    }

    private fun progressVisible(isVisible: Boolean) {
        search_progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

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