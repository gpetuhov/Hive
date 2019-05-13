package com.gpetuhov.android.hive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.gpetuhov.android.hive.domain.interactor.SaveActivityInteractor
import timber.log.Timber

// Receives intents with recognized user activity
class ActivityRecognitionReceiver : BroadcastReceiver() {

    private val saveActivityInteractor = SaveActivityInteractor()

    companion object {
        private const val TAG = "ActivityRecognition"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            if (result != null) processTransitionResult(result)
        }
    }

    // === Private methods ===

    private fun processTransitionResult(result: ActivityTransitionResult) {
        // We need only the latest event
        val event = result.transitionEvents.firstOrNull()

        if (event != null) {
            when (val activityType = event.activityType) {
                DetectedActivity.IN_VEHICLE,
                DetectedActivity.ON_BICYCLE,
                DetectedActivity.STILL,
                DetectedActivity.WALKING,
                DetectedActivity.RUNNING -> {
                    Timber.tag(TAG).d("Activity = $activityType")
                    saveActivityInteractor.saveActivity(activityType)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }
}
