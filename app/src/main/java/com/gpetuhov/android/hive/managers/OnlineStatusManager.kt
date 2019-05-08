package com.gpetuhov.android.hive.managers

import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.util.Constants
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class OnlineStatusManager(var repo: Repo) {

    private var userOnlineSub = PublishSubject.create<Boolean>()
    private var userOnlineSubDisposable: Disposable? = null

    init {
        startUserOnlineSub()
    }

    // === Public methods ===

    fun onResume(onConnectionStateChanged: (Boolean) -> Unit) {
        repo.setForeground(true)

        repo.startGettingConnectionStateUpdates { connected ->
            onConnectionStateChanged(connected)

            // This is needed to show other users that the user is online,
            // if the network goes off and back on,
            // while the user is in MainActivity
            // (MainActivity onResume state does not change).
            if (connected) setUserOnline()
        }

        // Others will see, that this user is online,
        // only when this user's MainActivity is in onResume state.
        setUserOnline()
    }

    fun onPause() {
        repo.setForeground(false)
        repo.stopGettingConnectionStateUpdates()

        // As soon, as this user's MainActivity switches in onPause state,
        // others should see that this user goes offline.
        setUserOffline()
    }

    // === Private methods ===

    private fun startUserOnlineSub() {
        // This debounce is needed to avoid rapid user online status change
        // on screen rotation (because on screen rotation MainActivity onPause and onResume are triggered).
        userOnlineSubDisposable = userOnlineSub
            .debounce(Constants.User.USER_STATUS_LATENCY, TimeUnit.MILLISECONDS)
            .subscribe { isOnline ->
                if (isOnline) {
                    repo.setUserOnline()
                } else {
                    repo.setUserOffline()
                }
            }
    }

    private fun setUserOnline() = userOnlineSub.onNext(true)

    private fun setUserOffline() = userOnlineSub.onNext(false)
}