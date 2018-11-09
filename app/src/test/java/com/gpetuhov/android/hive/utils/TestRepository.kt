package com.gpetuhov.android.hive.utils

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo

class TestRepository : Repo {
    var isSuccess = false
    var username = ""
    var service = ""
    var isVisible = false
    var isOnline = false
    var messageText = ""

    override fun onSignIn(user: User) {
    }

    override fun onSignOut() {
    }

    override fun currentUser(): MutableLiveData<User> = MutableLiveData()

    override fun secondUser(): MutableLiveData<User> = MutableLiveData()

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

    override fun startGettingSecondUserUpdates(uid: String) {
    }

    override fun stopGettingSecondUserUpdates() {
    }

    override fun initSecondUser(uid: String, name: String) {
    }

    override fun searchResult(): MutableLiveData<MutableMap<String, User>> = MutableLiveData()

    override fun search(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String, onComplete: () -> Unit) = onComplete()

    override fun stopGettingSearchResultUpdates() {
    }

    override fun initSearchUserDetails(uid: String) {
    }

    override fun messages(): MutableLiveData<MutableList<Message>> = MutableLiveData()

    override fun startGettingMessagesUpdates() {
    }

    override fun stopGettingMessagesUpdates() {
    }

    override fun sendMessage(messageText: String, onError: () -> Unit) {
        if (isSuccess) {
            this.messageText = messageText
        } else {
            onError()
        }
    }

    override fun chatrooms(): MutableLiveData<MutableList<Chatroom>> = MutableLiveData()

    override fun startGettingChatroomsUpdates() {
    }

    override fun stopGettingChatroomsUpdates() {
    }
}