package com.gpetuhov.android.hive.domain.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.*

// At domain layer (business logic layer) we have only interface of the repo.
// All classes at this layer call methods of this interface to retrieve and save data.
// The interface must be implemented at the storage (outer) layer.
interface Repo {

    // App status
    fun isForeground(): Boolean
    fun setForeground(value: Boolean)
    fun isChatroomListOpen(): Boolean
    fun setChatroomListOpen(value: Boolean)
    fun isChatroomOpen(secondUserUid: String): Boolean
    fun setChatroomOpen(value: Boolean)

    // Authentication
    fun onSignIn(user: User)
    fun onSignOut()

    // User
    fun currentUser(): MutableLiveData<User>
    fun secondUser(): MutableLiveData<User>
    fun currentUserUid(): String
    fun currentUserUsername(): String
    fun currentUserDescription(): String
    fun saveUserUsername(newUsername: String, onError: () -> Unit)
    fun saveUserDescription(newDescription: String, onError: () -> Unit)
    fun saveUserLocation(newLocation: LatLng)
    fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit)
    fun startGettingSecondUserUpdates(uid: String)
    fun stopGettingSecondUserUpdates()
    fun initSecondUser(uid: String, name: String, userPicUrl: String)
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

    // Unread messages
    fun unreadMessagesExist(): MutableLiveData<Boolean>
    fun setUnreadMessagesExist(value: Boolean)

    // User pic
    fun changeUserPic(selectedImageUri: Uri, onError: () -> Unit)

    // User photo
    fun addUserPhoto(selectedImageUri: Uri, onError: () -> Unit)
    fun deleteUserPhoto(photoUid: String, onError: () -> Unit)

    // Offer
    fun currentUserOfferList(): MutableList<Offer>
    fun saveOffer(offer: Offer?, onSuccess: () -> Unit, onError: () -> Unit)
    fun deleteOffer(offerUid: String, onSuccess: () -> Unit, onError: () -> Unit)
    fun addOfferPhoto(selectedImageUri: Uri, onSuccess: (Photo) -> Unit, onError: () -> Unit)
    fun deleteOfferPhotoFromStorage(photoUid: String)
    fun cancelPhotoUploadTasks()

    // Favorites
    fun favorites(): MutableLiveData<MutableList<Favorite>>
    fun favoriteUsers(): MutableLiveData<MutableList<User>>
    fun favoriteOffers(): MutableLiveData<MutableList<Offer>>
    fun addFavorite(userUid: String, offerUid: String, onError: () -> Unit)
    fun removeFavorite(userUid: String, offerUid: String, onError: () -> Unit)
}
