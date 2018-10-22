package com.gpetuhov.android.hive.service

import android.content.Intent
import android.os.IBinder
import timber.log.Timber
import android.app.Service
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
import javax.inject.Inject


// Shares current location.
// Location is updated on the server as long as LocationService is running.

class LocationService : Service() {

    @Inject lateinit var locationManager: LocationManager

    companion object {
        private const val TAG = "LocationService"
    }

    override fun onCreate() {
        Timber.tag(TAG).d("onCreate")
        super.onCreate()
        HiveApp.appComponent.inject(this)
        locationManager.startLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).d("onStartCommand")

        // This is needed for the service to be recreated, if killed by the OS
        return START_STICKY
    }

    override fun onDestroy() {
        Timber.tag(TAG).d("onDestroy")
        super.onDestroy()
        locationManager.stopLocationUpdates()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}