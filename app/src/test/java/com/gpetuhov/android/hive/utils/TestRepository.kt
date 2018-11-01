package com.gpetuhov.android.hive.utils

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo

class TestRepository : Repo {
    var isSuccess = false
    var username = ""
    var service = ""
    var isVisible = false
    var isOnline = false

    override fun onSignIn(user: User) {
    }

    override fun onSignOut() {
    }

    override fun currentUser(): MutableLiveData<User> = MutableLiveData()

    override fun currentUserUsername() = username

    override fun currentUserService() = service

    override fun saveUserUsername(newUsername: String, onError: () -> Unit) {
        if (isSuccess) {
            username = newUsername
        } else {
            onError()
        }
    }

    override fun saveUserService(newService: String, onError: () -> Unit) {
        if (isSuccess) {
            service = newService
        } else {
            onError()
        }
    }

    override fun deleteUserService(onError: () -> Unit) {
        if (isSuccess) {
            service = ""
        } else {
            onError()
        }
    }

    override fun saveUserVisibility(newIsVisible: Boolean, onError: () -> Unit) {
        if (isSuccess) {
            isVisible = newIsVisible
        } else {
            onError()
        }
    }

    override fun saveUserLocation(newLocation: LatLng) {
    }

    override fun saveUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit) {
        if (isSuccess) isOnline = newIsOnline
        onComplete()
    }

    override fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit) {
    }

    override fun searchResultList(): MutableLiveData<MutableList<User>> = MutableLiveData()

    override fun search(queryText: String, onComplete: () -> Unit) = onComplete()

    override fun stopGettingSearchResultUpdates() {
    }
}