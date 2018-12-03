package com.gpetuhov.android.hive.utils

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo

class TestRepository : Repo {
    var isSuccess = false
    var username = ""
    var description = ""
    var isOnline = false
    var messageText = ""
    var offerList = mutableListOf<Offer>()

    override fun isForeground() = false

    override fun setForeground(value: Boolean) {
    }

    override fun isChatroomListOpen() = false

    override fun setChatroomListOpen(value: Boolean) {
    }

    override fun isChatroomOpen(secondUserUid: String) = false

    override fun setChatroomOpen(value: Boolean) {
    }

    override fun onSignIn(user: User) {
    }

    override fun onSignOut() {
    }

    override fun currentUser(): MutableLiveData<User> = MutableLiveData()

    override fun secondUser(): MutableLiveData<User> = MutableLiveData()

    override fun currentUserUsername() = username

    override fun currentUserDescription() = ""

    override fun saveUserUsername(newUsername: String, onError: () -> Unit) {
        if (isSuccess) {
            username = newUsername
        } else {
            onError()
        }
    }

    override fun saveUserDescription(newDescription: String, onError: () -> Unit) {
        if (isSuccess) {
            description = newDescription
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

    override fun initSecondUser(uid: String, name: String, userPicUrl: String) {
    }

    override fun saveFcmToken(token: String) {
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

    override fun clearMessages() {
    }

    override fun chatrooms(): MutableLiveData<MutableList<Chatroom>> = MutableLiveData()

    override fun startGettingChatroomsUpdates() {
    }

    override fun stopGettingChatroomsUpdates() {
    }

    override fun unreadMessagesExist(): MutableLiveData<Boolean> = MutableLiveData()

    override fun setUnreadMessagesExist(value: Boolean) {
    }

    override fun changeUserPic(selectedImageUri: Uri, onError: () -> Unit) {
    }

    override fun currentUserOfferList(): MutableList<Offer> = offerList

    override fun saveOffer(offer: Offer?, onSuccess: () -> Unit, onError: () -> Unit) {
    }

    override fun deleteOffer(offerUid: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isSuccess) {
            offerList.clear()
            onSuccess()
        } else {
            onError()
        }
    }
}