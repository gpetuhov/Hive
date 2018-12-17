package com.gpetuhov.android.hive.utils

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.*
import com.gpetuhov.android.hive.domain.repository.Repo

class TestRepository : Repo {
    var isSuccess = false
    var username = ""
    var description = ""
    var messageText = ""
    var offerList = mutableListOf<Offer>()
    var photoList = mutableListOf<Photo>()
    var favoriteList = mutableListOf<Favorite>()

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
        if (isSuccess && offer != null) {
            offerList.add(offer)
            onSuccess()
        } else {
            onError()
        }
    }

    override fun deleteOffer(offerUid: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isSuccess) {
            offerList.clear()
            onSuccess()
        } else {
            onError()
        }
    }

    override fun addUserPhoto(selectedImageUri: Uri, onError: () -> Unit) {
    }

    override fun deleteUserPhoto(photoUid: String, onError: () -> Unit) {
        if (isSuccess) {
            photoList.clear()
        } else {
            onError()
        }
    }

    override fun addOfferPhoto(selectedImageUri: Uri, onSuccess: (Photo) -> Unit, onError: () -> Unit) {
    }

    override fun deleteOfferPhotoFromStorage(photoUid: String) {
    }

    override fun cancelPhotoUploadTasks() {
    }

    override fun favorites(): MutableLiveData<MutableList<Favorite>> = MutableLiveData()

    override fun addFavorite(userUid: String, offerUid: String, onError: () -> Unit) {
        if (isSuccess) {
            favoriteList.add(Favorite(userUid, offerUid))
        } else {
            onError()
        }
    }

    override fun removeFavorite(userUid: String, offerUid: String, onError: () -> Unit) {
        if (isSuccess) {
            val index = favoriteList.indexOfFirst { it.userUid == userUid && it.offerUid == offerUid }
            if (index >= 0 && index < favoriteList.size) favoriteList.removeAt(index)
        } else {
            onError()
        }
    }
}