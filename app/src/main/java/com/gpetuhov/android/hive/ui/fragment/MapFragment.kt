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
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentMapBinding
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.MapFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

class MapFragment :
    MvpAppCompatFragment(),
    MapFragmentView,
    MapManager.Callback {

    @InjectPresenter lateinit var presenter: MapFragmentPresenter

    @Inject lateinit var mapManager: MapManager

    private var mapView: MapView? = null
    private var binding: FragmentMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding?.presenter = presenter

        val view = binding?.root

        mapView = view?.findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)

        mapManager.restoreMapState(savedInstanceState)

        // Asynchronously get reference to the map
        mapView?.getMapAsync(this::onMapReady)

        return view
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
        mapManager.saveOutState(outState)
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

        // TODO: hide keyboard
        // TODO: (or hide keyboard with a separate command, and don't put this command in the queue ???)
    }

    override fun onSearchComplete() {
        progressVisible(false)
        buttonsEnabled(true)

        // TODO: hide keyboard
    }

    override fun clearSearch() = query_text.setText("")

    override fun showToast(message: String) {
        toast(message)
    }

    // === MapManager.Callback ===
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

    // === Private methods ===

    private fun onMapReady(map: GoogleMap) {
        mapManager.initMap(this, map)

        mapControlsVisible()

        // Start observing result list ViewModel only after map is ready
        val viewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        viewModel.resultList.observe(this, Observer<MutableList<User>> { resultList ->
            mapManager.updateMarkers(context, resultList)
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