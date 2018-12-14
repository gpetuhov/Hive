package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentLocationBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.LocationFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.LocationFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.*
import timber.log.Timber

class LocationFragment : BaseFragment(), LocationFragmentView {

    @InjectPresenter lateinit var presenter: LocationFragmentPresenter

    private var mapView: MapView? = null
    private var binding: FragmentLocationBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        binding?.presenter = presenter

        presenter.userUid = LocationFragmentArgs.fromBundle(arguments).userUid

        val view = binding?.root
        mapView = view?.findViewById(R.id.location_map_view)
        mapView?.onCreate(savedInstanceState)

        mapView?.getMapAsync(this::onMapReady)

        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        presenter.onResume()
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
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    // === LocationFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun onMapReady(googleMap: GoogleMap) {
        // TODO: refactor this out of fragment

        try {
            // Show my location (blue point)
            googleMap.isMyLocationEnabled = true

        } catch (e: SecurityException) {
            Timber.tag("LocationFragment").d("Location permission not granted")
        }

        // Enable compass (will show on map rotate)
        googleMap.uiSettings.isCompassEnabled = true

        // Enable my location button
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        // Enable zoom buttons
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Enable toolbar that opens Google Maps App
        googleMap.uiSettings.isMapToolbarEnabled = true

        // Set minimum and maximum zoom
        googleMap.setMinZoomPreference(Constants.Map.MIN_ZOOM)
        googleMap.setMaxZoomPreference(Constants.Map.MAX_ZOOM)



        val viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.updateLocation(user.location)

            googleMap.moveCamera(user.location)
            googleMap.addSingleMarker(user.location)
        })
    }
}