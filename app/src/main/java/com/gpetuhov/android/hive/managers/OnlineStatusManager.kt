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

    fun setUserOnline() = userOnlineSub.onNext(true)

    fun setUserOffline() = userOnlineSub.onNext(false)

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
}