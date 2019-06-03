package com.gpetuhov.android.hive.service

import android.content.Intent
import android.os.IBinder
import timber.log.Timber
import android.app.Service
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveHiveRunningInteractor
import com.gpetuhov.android.hive.managers.ActivityRecognitionManager
import com.gpetuhov.android.hive.managers.LocationManager
import javax.inject.Inject
import com.gpetuhov.android.hive.managers.NotificationManager


// Shares current location.
// Location is updated on the server as long as LocationService is running.

class LocationService : Service() {

    companion object {
        private const val TAG = "LocationService"
    }

    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var notificationManager: NotificationManager
    @Inject lateinit var activityRecognitionManager: ActivityRecognitionManager

    private val saveHiveRunningInteractor = SaveHiveRunningInteractor()

    override fun onCreate() {
        Timber.tag(TAG).d("onCreate")
        super.onCreate()
        HiveApp.appComponent.inject(this)
        locationManager.startLocationUpdates()
        activityRecognitionManager.startActivityRecognition()
        saveHiveRunningInteractor.saveHiveRunning(true)

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
        activityRecognitionManager.stopActivityRecognition()
        saveHiveRunningInteractor.saveHiveRunning(false)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundService() {
        // This is needed to prevent service from being killed by the OS
        startForeground(
            NotificationManager.LOCATION_SHARING_NOTIFICATION_ID,
            notificationManager.getLocationSharingNotification()
        )
    }
}