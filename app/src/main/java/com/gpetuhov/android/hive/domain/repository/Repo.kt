package com.gpetuhov.android.hive.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.model.User

// At domain layer (business logic layer) we have only interface of the repo.
// All classes at this layer call methods of this interface to retrieve and save data.
// The interface must be implemented at the storage (outer) layer.
interface Repo {

    // App status
    fun isForeground(): Boolean
    fun setForeground(value: Boolean)
    fun isChatroomListOpen(): Boolean
    fun setChatroomListOpen(value: Boolean)
    fun isChatroomOpen(): Boolean
    fun setChatroomOpen(value: Boolean)

    // Authentication
    fun onSignIn(user: User)
    fun onSignOut()

    // User
    fun currentUser(): MutableLiveData<User>
    fun secondUser(): MutableLiveData<User>
    fun currentUserUsername(): String
    fun currentUserService(): String
    fun saveUserVisibility(newIsVisible: Boolean, onError: () -> Unit)
    fun saveUserUsername(newUsername: String, onError: () -> Unit)
    fun saveUserService(newService: String, onError: () -> Unit)
    fun deleteUserService(onError: () -> Unit)
    fun saveUserLocation(newLocation: LatLng)
    fun saveUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit)
    fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit)
    fun startGettingSecondUserUpdates(uid: String)
    fun stopGettingSecondUserUpdates()
    fun initSecondUser(uid: String, name: String)
    fun saveFcmToken(token: String)

    // Search
    fun searchResult(): MutableLiveData<MutableMap<String, User>>
    fun search(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String, onComplete: () -> Unit)
    fun stopGettingSearchResultUpdates()
    fun initSearchUserDetails(uid: String)

    // Message
    fun messages(): MutableLiveData<MutableList<Message>>
    fun startGettingMessagesUpdates()
    fun stopGettingMessagesUpdates()
    fun sendMessage(messageText: String, onError: () -> Unit)
    fun clearMessages()

    // Chatroom
    fun chatrooms(): MutableLiveData<MutableList<Chatroom>>
    fun startGettingChatroomsUpdates()
    fun stopGettingChatroomsUpdates()
}