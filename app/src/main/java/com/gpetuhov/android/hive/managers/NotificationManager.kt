package com.gpetuhov.android.hive.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.ui.activity.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// Show notifications
class NotificationManager {

    companion object {
        const val LOCATION_SHARING_NOTIFICATION_ID = 1001
        const val NEW_MESSAGE_NOTIFICATION_ID = 1002
        private const val LOCATION_SHARING_CHANNEL = "location_sharing_channel"
        private const val NEW_MESSAGE_CHANNEL = "new_message_channel"
        private const val NOTIFICATION_BUFFER_TIME = 5000L
    }

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    private var notificationManager: NotificationManager
    private var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null

    private var notificationSub = PublishSubject.create<NotificationInfo>()
    private var notificationSubDisposable: Disposable? = null

    init {
        HiveApp.appComponent.inject(this)

        notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        createLocationSharingNotificationChannel()
        createNewMessageNotificationChannel()

        startNotificationSub()
    }

    // === Public methods ===

    fun getLocationSharingNotification(): Notification? {
        val builder = NotificationCompat.Builder(context, LOCATION_SHARING_CHANNEL)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.location_sharing_enabled))
            .setSmallIcon(R.drawable.android_round)
            .setContentIntent(getMainActivityPendingIntent())

        return builder.build()
    }

    // Do not notify on every new message.
    // Buffer notifications and notify only once per period of time.
    fun showNewMessageNotification(senderUid: String, senderName: String, messageText: String, messageTimestamp: Long) =
        notificationSub.onNext(NotificationInfo(senderUid, senderName, messageText, messageTimestamp))

    // === Lifecycle calls ===

    fun onResume() {
        cancelNewMessageNotification()

        // Sounds are taken from:
        // https://notificationsounds.com/notification-sounds/plucky-564
        mediaPlayer = createMediaPlayer(R.raw.plucky)
    }

    fun onPause() {
        mediaPlayer?.release()
        mediaPlayer = null
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

    private fun createMediaPlayer(soundId: Int): MediaPlayer {
        val fileDescriptor = context.resources.openRawResourceFd(soundId)
        val player = MediaPlayer()
        player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
        player.setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
        player.prepare()
        return player
    }

    private fun startNotificationSub() {
        notificationSubDisposable = notificationSub
            .buffer(NOTIFICATION_BUFFER_TIME, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { notificationInfoList ->
                if (!notificationInfoList.isEmpty()) {
                    Timber.tag("NotificationManager").d("${notificationInfoList.size}")
                    var latestNotificationInfo = notificationInfoList[0]

                    val senderUidList = mutableListOf<String>()

                    // Choose latest notification info
                    for (notificationInfo in notificationInfoList) {
                        if (notificationInfo.messageTimestamp > latestNotificationInfo.messageTimestamp) {
                            latestNotificationInfo = notificationInfo
                        }

                        // Save all sender uids in a list
                        if (!senderUidList.contains(notificationInfo.senderUid)) senderUidList.add(notificationInfo.senderUid)
                    }

                    notify(latestNotificationInfo, senderUidList)
                }
            }
    }

    private fun notify(notificationInfo: NotificationInfo, senderUidList: MutableList<String>) {
        if (repo.isForeground()) {
            // If the app is in the foreground,

            // If chatroom list is not open
            // and user is not in chatroom with at least one sender,
            // notify user without showing notification (play sound)
            // and set unread messages flag.
            if (!repo.isChatroomListOpen() && atLeastOneSenderNotInChatroom(senderUidList)) {
                mediaPlayer?.start()
                repo.setUnreadMessagesExist(true)
            }

        } else {
            // If the app is in background, show notification
            // and set unread messages flag.
            val builder = NotificationCompat.Builder(context, NEW_MESSAGE_CHANNEL)
                .setContentTitle(notificationInfo.senderName)
                .setContentText(notificationInfo.messageText)
                .setSmallIcon(R.drawable.android_round)
                .setContentIntent(getMainActivityPendingIntent())
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            notificationManager.notify(NEW_MESSAGE_NOTIFICATION_ID, builder.build())

            repo.setUnreadMessagesExist(true)
        }
    }

    private fun atLeastOneSenderNotInChatroom(senderUidList: MutableList<String>): Boolean {
        // Check if sender uid list has at least one uid with not open chatroom
        var result = false

        for (senderUid in senderUidList) {
            if (!repo.isChatroomOpen(senderUid)) {
                result = true
                break
            }
        }

        return result
    }

    private fun getMainActivityPendingIntent(): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    // === Inner classes ===

    private class NotificationInfo(
        val senderUid: String,
        val senderName: String,
        val messageText: String,
        val messageTimestamp: Long
    )
}