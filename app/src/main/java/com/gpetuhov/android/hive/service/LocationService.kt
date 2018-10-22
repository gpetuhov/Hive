package com.gpetuhov.android.hive.service

import android.content.Intent
import android.os.IBinder
import timber.log.Timber
import android.app.Service


// Shares current location
class LocationService : Service() {

    companion object {
        private const val TAG = "LocationService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).d("onStartCommand")

        // This is needed for the service to be recreated, if killed by the OS
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}