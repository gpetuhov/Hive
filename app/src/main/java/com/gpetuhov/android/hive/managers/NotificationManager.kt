package com.gpetuhov.android.hive.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.ui.activity.MainActivity
import javax.inject.Inject

// Show notifications
class NotificationManager {

    companion object {
        const val LOCATION_SHARING_NOTIFICATION_ID = 1001
        const val NEW_MESSAGE_NOTIFICATION_ID = 1002
        private const val LOCATION_SHARING_CHANNEL = "location_sharing_channel"
        private const val NEW_MESSAGE_CHANNEL = "new_message_channel"
    }

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    private var notificationManager: NotificationManager
    private var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayerLight: MediaPlayer? = null

    init {
        HiveApp.appComponent.inject(this)

        notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        createLocationSharingNotificationChannel()
        createNewMessageNotificationChannel()
    }

    // === Public methods ===

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

    fun showNewMessageNotification(senderUid: String, senderName: String, messageText: String) {
        if (repo.isForeground()) {
            // If the app is in the foreground,
            // and chatroom list is not open,
            // and chatroom, new chat message belongs to, is not open,
            // notify user without showing notification (sound or vibrate).
            // In other cases user will be notified by the corresponding listeners
            // (so that sound or vibration will be triggered at the moment of the UI change.

            if (!repo.isChatroomListOpen() && !repo.isChatroomOpen(senderUid)) {
                notifyNewMessageFromInsideTheApp(false)
            }

        } else {
            // If the app is in background, show notification

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            val builder = NotificationCompat.Builder(context, NEW_MESSAGE_CHANNEL)
                .setContentTitle(senderName)
                .setContentText(messageText)
                .setSmallIcon(R.drawable.android_round)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            notificationManager.notify(NEW_MESSAGE_NOTIFICATION_ID, builder.build())
        }
    }

    fun notifyNewMessageFromInsideChatOrChatroomList() = notifyNewMessageFromInsideTheApp(true)

    // === Lifecycle calls ===

    fun onResume() {
        cancelNewMessageNotification()

        // Sounds are taken from:
        // https://notificationsounds.com/notification-sounds/light-562
        // https://notificationsounds.com/notification-sounds/plucky-564
        mediaPlayer = MediaPlayer.create(context, R.raw.plucky)
        mediaPlayerLight = MediaPlayer.create(context, R.raw.light)
    }

    fun onPause() {
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayerLight?.release()
        mediaPlayerLight = null
    }

    // === Private methods ===

    private fun createLocationSharingNotificationChannel() {
        createNotificationChannel(
            LOCATION_SHARING_CHANNEL,
            R.string.location_sharing_channel_name,
            R.string.location_sharing_channel_description,
            false
        )
    }

    private fun createNewMessageNotificationChannel() {
        createNotificationChannel(
            NEW_MESSAGE_CHANNEL,
            R.string.new_message_channel_name,
            R.string.new_message_channel_description,
            true
        )
    }

    private fun createNotificationChannel(channelId: String, channelNameId: Int, channelDescriptionId: Int, isImportanceHigh: Boolean) {
        // Notification channels are needed since Android Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = context.getString(channelNameId)
            val descriptionText = context.getString(channelDescriptionId)

            // Importance must be high for the notifications to show up at the top of the screen
            val importance =
                if (isImportanceHigh) android.app.NotificationManager.IMPORTANCE_HIGH
                else android.app.NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(channelId, name, importance)
            channel.description = descriptionText

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun cancelNewMessageNotification() = notificationManager.cancel(NEW_MESSAGE_NOTIFICATION_ID)

    private fun notifyNewMessageFromInsideTheApp(isLight: Boolean) {
        val tempMediaPlayer = if (isLight) mediaPlayerLight else mediaPlayer
        tempMediaPlayer?.start()
    }
}