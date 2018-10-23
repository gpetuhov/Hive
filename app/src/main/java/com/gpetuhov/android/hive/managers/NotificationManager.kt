package com.gpetuhov.android.hive.managers

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.ui.activity.MainActivity
import javax.inject.Inject

class NotificationManager {

    companion object {
        const val LOCATION_SHARING_NOTIFICATION_ID = 1001
        private const val DEFAULT_CHANNEL = "default_channel"
    }

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    fun getLocationSharingNotification(): Notification? {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context, DEFAULT_CHANNEL)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.location_sharing_enabled))
            .setSmallIcon(R.drawable.android_round)
            .setContentIntent(pendingIntent)

        return builder.build()
    }
}