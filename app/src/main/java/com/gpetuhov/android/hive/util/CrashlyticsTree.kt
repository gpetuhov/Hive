package com.gpetuhov.android.hive.util

import com.crashlytics.android.Crashlytics
import timber.log.Timber

// Writes Timber logs to Crashlytics
class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        Crashlytics.log(priority, tag, message)

        if (throwable != null) {
            Crashlytics.logException(throwable)
        }
    }
}