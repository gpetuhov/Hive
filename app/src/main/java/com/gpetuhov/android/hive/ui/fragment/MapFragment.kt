package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.ui.viewmodel.SearchResultViewModel
import javax.inject.Inject

class MapFragment : Fragment() {

    @Inject lateinit var mapManager: MapManager
    @Inject lateinit var repo: Repository

    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.map_view)
        mapView?.onCreate(savedInstanceState)

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
        repo.startGettingRemoteResultUpdates()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        repo.stopGettingRemoteResultUpdates()
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
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun onMapReady(map: GoogleMap) {
        mapManager.initMap(map)

        // Start observing result list ViewModel only after map is ready
        val viewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        viewModel.resultList.observe(this, Observer<MutableList<User>> { resultList ->
            mapManager.updateMarkers(context, resultList)
        })
    }
}