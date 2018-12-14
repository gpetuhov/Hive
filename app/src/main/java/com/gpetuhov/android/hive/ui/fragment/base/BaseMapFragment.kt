package com.gpetuhov.android.hive.ui.fragment.base

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

abstract class BaseMapFragment : BaseFragment(), OnMapReadyCallback {

    private var mapView: MapView? = null

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
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

    // === Protected methods ===

    protected fun initMap(rootView: View?, mapViewId: Int, savedInstanceState: Bundle?) {
        mapView = rootView?.findViewById(mapViewId)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }
}