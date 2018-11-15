package com.gpetuhov.android.hive.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessageService : FirebaseMessagingService() {

    // Called when message is received.
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

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
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        // Get text from received message
        val messageText = remoteMessage?.notification?.body
        val messageData = remoteMessage?.data.toString()

        Timber.tag("MessageService").d("messageText = $messageText")
        Timber.tag("MessageService").d("messageData = $messageData")
    }

    override fun onNewToken(token: String?) {
        // This one is called when new FCM token is received

        Timber.tag("MessageService").d("Token = $token")
    }
}