package com.gpetuhov.android.hive.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.gpetuhov.android.hive.receiver.ActivityRecognitionReceiver
import timber.log.Timber

// Recognizes user activity (Still, walk, run etc.)
class ActivityRecognitionManager(private val context: Context) {

    companion object {
        private const val TAG = "ActivityRecognition"
    }

    private var pendingIntent: PendingIntent? = null

    // === Public methods ===

    fun startActivityRecognition() {
        val request = getActivityRecognitionRequest()
        pendingIntent = getActivityRecognitionIntent()

        ActivityRecognition.getClient(context)
            .requestActivityTransitionUpdates(request, pendingIntent)
            .addOnSuccessListener {
                // Results will go into ActivityRecognitionReceiver
                Timber.tag(TAG).d("Activity recognition started successfully")
            }
            .addOnFailureListener {
                Timber.tag(TAG).d("Failed to start activity recognition")
            }
    }

    fun stopActivityRecognition() {
        ActivityRecognition.getClient(context)
            .removeActivityTransitionUpdates(pendingIntent)
            .addOnSuccessListener {
                Timber.tag(TAG).d("Activity recognition stopped successfully")
            }
            .addOnFailureListener {
                Timber.tag(TAG).d("Failed to stop activity recognition")
            }
    }

    // === Private methods ===

    private fun getActivityRecognitionRequest() : ActivityTransitionRequest {
        // We detect these activities
        val activities = listOf(
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.STILL,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING
        )

        val transitions = mutableListOf<ActivityTransition>()

        activities.forEach { activity ->
            // We detect only when user starts doing something (enters transition)
            val enterTransition = ActivityTransition.Builder()
                .setActivityType(activity)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()

            transitions.add(enterTransition)
        }

        return ActivityTransitionRequest(transitions)
    }

    private fun getActivityRecognitionIntent() : PendingIntent {
        val intent = Intent(context, ActivityRecognitionReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}