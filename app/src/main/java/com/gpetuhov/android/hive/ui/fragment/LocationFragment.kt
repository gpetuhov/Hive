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
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentLocationBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.LocationFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.LocationFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.*
import timber.log.Timber

class LocationFragment : BaseMapFragment(), LocationFragmentView {

    @InjectPresenter lateinit var presenter: LocationFragmentPresenter

    private var binding: FragmentLocationBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        binding?.presenter = presenter

        presenter.userUid = LocationFragmentArgs.fromBundle(arguments).userUid

        val rootView = binding?.root

        initMap(rootView, R.id.location_map_view, savedInstanceState)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    // === LocationFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === OnMapReadyCallback ===

    override fun onMapReady(googleMap: GoogleMap) {
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