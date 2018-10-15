package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.gpetuhov.android.hive.R

class MapFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Asynchronously get reference to the map
        mapView.getMapAsync { map ->
            // When the map is ready, get reference to it
            googleMap = map

            // Enable zoom buttons
            googleMap.uiSettings.isZoomControlsEnabled = true

            // When the map is loaded, do something
            googleMap.setOnMapLoadedCallback {
                // TODO: do something
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}