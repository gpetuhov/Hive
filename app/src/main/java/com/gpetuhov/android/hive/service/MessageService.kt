package com.gpetuhov.android.hive.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessageService : FirebaseMessagingService() {

    // Called when message is received.
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        // This method is called only if the message is received, when the app is in the FOREGROUND.
        // When the app is in the background, a standard notification
        // is displayed in the system tray and onMessageReceived is NOT called.
        // If the message, received when the app is in the background,
        // contains data payload, it can be retrieved
        // from the intent launched when the user taps on the notification.

        // Get text from received message
        val messageText = remoteMessage?.notification?.body

        Timber.tag("MessageService").d("Received message: $messageText")
    }
}