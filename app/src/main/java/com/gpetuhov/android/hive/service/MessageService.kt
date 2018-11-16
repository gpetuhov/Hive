package com.gpetuhov.android.hive.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.NotificationManager
import timber.log.Timber
import javax.inject.Inject

class MessageService : FirebaseMessagingService() {

    @Inject lateinit var repo: Repo
    @Inject lateinit var notificationManager: NotificationManager

    companion object {
        private const val TAG = "MessageService"
    }

    // There are two types of messages data messages and notification messages. Data messages
    // are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data
    // messages are the type
    // traditionally used with GCM. Notification messages are only received here in
    // onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated
    // notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages
    // containing both notification
    // and data payloads are treated as notification messages. The Firebase console always
    // sends notification

    // In this app we send DATA messages using Firebase Cloud Functions every time,
    // new message document is created in Firestore chatrooms collection.
    // Cloud Functions code is hosted in a SEPARATE repository.

    // Note that FCM messages are NOT delivered to the app,
    // if the app has been FORCE CLOSED (not swiped away).
    // (on some devices, the app is force closed if swiped away).

    override fun onCreate() {
        super.onCreate()
        HiveApp.appComponent.inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val messageData = remoteMessage?.data
        Timber.tag(TAG).d("messageData = $messageData")

        if (messageData != null) {
            val senderName = messageData["senderName"] ?: ""
            val messageText = messageData["messageText"] ?: ""

            if (senderName != "" && messageText != "") {
                notificationManager.showNewMessageNotification(senderName, messageText)
            }
        }
    }

    // This one is called when new FCM token is received.
    // This token is used to send new message notifications only to that user,
    // who is the receiver of this message (inside the Cloud Functions code).
    override fun onNewToken(token: String?) {
        if (token != null) {
            Timber.tag(TAG).d("Token = $token")
            repo.saveFcmToken(token)
        }
    }
}