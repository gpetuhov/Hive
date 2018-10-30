package com.gpetuhov.android.hive.utils

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo

class TestRepository : Repo {
    var isSuccess = false
    var username = ""
    var service = ""

    override fun onSignIn(user: User) {
    }

    override fun onSignOut() {
    }

    override fun currentUser(): MutableLiveData<User> = MutableLiveData()

    override fun currentUserUsername() = username

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

    override fun saveUserLocation(newLocation: LatLng) {
    }

    override fun saveUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit) {
    }

    override fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit) {
    }

    override fun resultList(): MutableLiveData<MutableList<User>> = MutableLiveData()

    override fun startGettingRemoteResultUpdates() {
    }

    override fun stopGettingRemoteResultUpdates() {
    }
}