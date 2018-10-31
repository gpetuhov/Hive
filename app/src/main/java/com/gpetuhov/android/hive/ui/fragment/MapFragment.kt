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
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.presenter.MapFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

class MapFragment : MvpAppCompatFragment(), MapFragmentView {

    @InjectPresenter lateinit var presenter: MapFragmentPresenter

    @Inject lateinit var mapManager: MapManager
    @Inject lateinit var repo: Repo

    private var mapView: MapView? = null
    private var binding: FragmentMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding?.handler = this

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
        search()
    }

    override fun onPause() {
        super.onPause()

        // Save map state here, because onPause() is guaranteed to be called
        mapManager.saveMapState()

        mapView?.onPause()
        repo.stopGettingSearchResultUpdates()
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
    }

    override fun onSearchComplete() {
        progressVisible(false)
        buttonsEnabled(true)

        // TODO: hide keyboard
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Public methods ===

    // TODO: refactor this into presenter
    fun search() {
        repo.search(query_text.text.toString())
    }

    fun cancelSearch() {
        query_text.setText("")
        search()
    }

    // === Private methods ===

    private fun onMapReady(map: GoogleMap) {
        mapManager.initMap(map)

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
}