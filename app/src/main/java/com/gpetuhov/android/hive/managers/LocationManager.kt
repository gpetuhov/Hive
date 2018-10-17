package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationManager(val context: Context) {

    companion object {
        private const val TAG = "LocationManager"
    }

    var currentLocation: LatLng = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    init {
        createLocationCallback()
        createLocationRequest()
    }

    // Check if location is turned on in system settings.
    // Prompt the user to change settings if turned off.
    // Result is passed to onActivityResult() of the calling activity.
    fun checkLocationSettings(activity: Activity, resultCode: Int) {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            Timber.tag(TAG).d("Geolocation is on")
        }

        task.addOnFailureListener { exception ->
            Timber.tag(TAG).d("Geolocation is off")

            if (exception is ResolvableApiException) {
                Timber.tag(TAG).d("Prompt the user to turn on geolocation")

                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, resultCode)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Timber.tag(TAG).d("Error showing location settings dialog")
                }
            }
        }
    }

    fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (e: SecurityException) {
            Timber.tag(TAG).d("Location permission not granted")
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val location = locationResult.lastLocation
                    currentLocation = LatLng(location.latitude, location.longitude)
                    Timber.tag(TAG).d("${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = Constants.Location.UPDATE_INTERVAL
            fastestInterval = Constants.Location.UPDATE_FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}