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
import android.location.Location
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.repository.Repository
import javax.inject.Inject


class LocationManager(context: Context) {

    companion object {
        private const val TAG = "LocationManager"
    }

    @Inject lateinit var repo: Repository
    @Inject lateinit var authManager: AuthManager

    var currentLocation: LatLng = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    init {
        HiveApp.appComponent.inject(this)
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

    fun getLastLocation(onSuccess: (LatLng) -> Unit) {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    Timber.tag(TAG).d("Received last known location")
                    saveLocation(location)
                    onSuccess(currentLocation)
                }

        } catch (e: SecurityException) {
            Timber.tag(TAG).d("Location permission not granted")
        }
    }

    // Check if Google Play Services installed
    fun checkPlayServices(activity: Activity, onUnrecoverableError: () -> Unit): Boolean {
        var servicesAvailable = true
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS) {
            servicesAvailable = false
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(activity, resultCode, 1).show()
            } else {
                onUnrecoverableError()
            }
        }

        return servicesAvailable
    }

    private fun saveLocation(location: Location?) {
        if (location != null) {
            currentLocation = LatLng(location.latitude, location.longitude)
            authManager.user.location = currentLocation
            repo.updateUserLocation(currentLocation,  { /* Do nothing */ }, { /* Do nothing */ })
            Timber.tag(TAG).d("${location.latitude}, ${location.longitude}")
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    saveLocation(locationResult.lastLocation)
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