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
    var reviewText = ""
    var rating = 0.0F
    var reviewList = mutableListOf<Review>()
    var commentText = ""
    var phone = ""
    var email = ""

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

    override fun currentUserUid() = ""

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

    override fun currentUserPhone() = phone

    override fun currentUserVisibleEmail() = email

    override fun saveUserPhone(newPhone: String, onError: () -> Unit) {
        if (isSuccess) {
            phone = newPhone
        } else {
            onError()
        }
    }

    override fun saveUserVisibleEmail(newEmail: String, onError: () -> Unit) {
        if (isSuccess) {
            email = newEmail
        } else {
            onError()
        }
    }

    override fun startGettingSecondUserUpdates(uid: String) {
    }

    override fun stopGettingSecondUserUpdates() {
    }

    override fun startGettingSecondUserOfferUpdates(uid: String) {
    }

    override fun stopGettingSecondUserOfferUpdates() {
    }

    override fun startGettingSecondUserLocationUpdates(uid: String) {
    }

    override fun stopGettingSecondUserLocationUpdates() {
    }

    override fun startGettingSecondUserChatUpdates(uid: String) {
    }

    override fun stopGettingSecondUserChatUpdates() {
    }

    override fun initSecondUser(uid: String, name: String, userPicUrl: String) {
    }

    override fun saveFcmToken(token: String) {
    }

    override fun searchResult(): MutableLiveData<MutableMap<String, User>> = MutableLiveData()

    override fun search(
        queryLatitude: Double,
        queryLongitude: Double,
        queryRadius: Double,
        queryText: String,
        onComplete: () -> Unit
    ) = onComplete()

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

    override fun favoriteUsers(): MutableLiveData<MutableList<User>> = MutableLiveData()

    override fun favoriteOffers(): MutableLiveData<MutableList<Offer>> = MutableLiveData()

    override fun loadFavorites(onComplete: () -> Unit) {
    }

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

    override fun initUserDetailsFromFavorites(uid: String) {
    }

    override fun saveReview(
        reviewUid: String,
        offerUid: String,
        text: String,
        rating: Float,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (isSuccess) {
            this.reviewText = text
            this.rating = rating
            onSuccess()
        } else {
            onError()
        }
    }

    override fun reviews(): MutableLiveData<MutableList<Review>> = MutableLiveData()

    override fun startGettingReviewsUpdates(offerUid: String, isCurrentUser: Boolean) {
    }

    override fun stopGettingReviewsUpdates() {
    }

    override fun clearReviews() {
    }

    override fun deleteReview(offerUid: String, reviewUid: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isSuccess) {
            reviewList.clear()
            onSuccess()
        } else {
            onError()
        }
    }

    override fun saveComment(reviewUid: String, offerUid: String, commentText: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isSuccess) {
            this.commentText = commentText
            onSuccess()
        } else {
            onError()
        }
    }
}