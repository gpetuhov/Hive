package com.gpetuhov.android.hive.service

import android.content.Intent
import android.os.IBinder
import timber.log.Timber
import android.app.Service
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
import javax.inject.Inject
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.activity.MainActivity


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

        startForegroundService()
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

    private fun startForegroundService() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(this, "Default")

        builder.setContentTitle(getString(R.string.app_name))
        builder.setContentText(getString(R.string.location_sharing_enabled))
        builder.setSmallIcon(R.drawable.android_round)
        builder.setContentIntent(pendingIntent)

        val notification = builder.build()

        // This is needed to prevent service from being killed by the OS
        startForeground(1, notification)
    }
}