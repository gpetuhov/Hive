package com.gpetuhov.android.hive.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import timber.log.Timber
import android.app.PendingIntent
import com.gpetuhov.android.hive.ui.activity.MainActivity


// Shares current location
class LocationService : Service() {

    companion object {
        private const val TAG = "LocationService"
        private const val SERVICE_NOTIFICATION_ID = 100
    }

    override fun onCreate() {
        super.onCreate()
        Timber.tag(TAG).d("onCreate")
        startForegroundAndShowNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).d("onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForegroundAndShowNotification() {
        // This is needed to prevent service from being killed by the OS
        startForeground(SERVICE_NOTIFICATION_ID, getNotification())
    }

    private fun getNotification(): Notification? {
        return NotificationCompat.Builder(this, "Default")
            .setContentTitle("Notification")
            .setContentText("Text text text")
            .setContentIntent(getMainActivityPendingIntent())
            .build()
    }

    private fun getMainActivityPendingIntent(): PendingIntent {
        val updateIntent = Intent(this, MainActivity::class.java)
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return PendingIntent.getActivity(this, 0, updateIntent, 0)
    }
}