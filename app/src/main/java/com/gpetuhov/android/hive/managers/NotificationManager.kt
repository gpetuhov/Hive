package com.gpetuhov.android.hive.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.ui.activity.MainActivity
import javax.inject.Inject

// Show notifications
class NotificationManager {

    companion object {
        const val LOCATION_SHARING_NOTIFICATION_ID = 1001
        private const val LOCATION_SHARING_CHANNEL = "location_sharing_channel"
    }

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
        createDefaultNotificationChannel()
    }

    fun getLocationSharingNotification(): Notification? {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context, LOCATION_SHARING_CHANNEL)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.location_sharing_enabled))
            .setSmallIcon(R.drawable.android_round)
            .setContentIntent(pendingIntent)

        return builder.build()
    }

    private fun createDefaultNotificationChannel() {
        // Notification channels are needed since Android Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = context.getString(R.string.location_sharing_channel_name)
            val descriptionText = context.getString(R.string.location_sharing_channel_description)
            val importance = android.app.NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(LOCATION_SHARING_CHANNEL, name, importance)
            channel.description = descriptionText

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}