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
    var skype = ""
    var facebook = ""
    var twitter = ""
    var instagram = ""
    var youtube = ""
    var website = ""
    var residence = ""
    var language = ""
    var education = ""
    var work = ""
    var interests = ""
    var status = ""
    var activity = Constants.DEFAULT_ACTIVITY
    var location = com.gpetuhov.android.hive.util.Constants.Map.DEFAULT_LOCATION
    var awardCongratulationShownList = mutableListOf<Int>()

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
        if (isSuccess) {
            location = newLocation
        }
    }

    override fun currentUserPhone() = phone

    override fun currentUserVisibleEmail() = email

    override fun currentUserEmail() = ""

    override fun currentUserSkype() = skype

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

    override fun saveUserSkype(newSkype: String, onError: () -> Unit) {
        if (isSuccess) {
            skype = newSkype
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

    override fun getAllUserReviews(isCurrentUser: Boolean, onComplete: (MutableList<Review>) -> Unit) {
    }

    override fun currentUserFacebook() = facebook

    override fun saveUserFacebook(newFacebook: String, onError: () -> Unit) {
        if (isSuccess) {
            facebook = newFacebook
        } else {
            onError()
        }
    }

    override fun currentUserTwitter(): String = twitter

    override fun saveUserTwitter(newTwitter: String, onError: () -> Unit) {
        if (isSuccess) {
            twitter = newTwitter
        } else {
            onError()
        }
    }

    override fun currentUserInstagram(): String = instagram

    override fun saveUserInstagram(newInstagram: String, onError: () -> Unit) {
        if (isSuccess) {
            instagram = newInstagram
        } else {
            onError()
        }
    }

    override fun currentUserYouTube(): String = youtube

    override fun saveUserYouTube(newYouTube: String, onError: () -> Unit) {
        if (isSuccess) {
            youtube = newYouTube
        } else {
            onError()
        }
    }

    override fun currentUserWebsite(): String = website

    override fun saveUserWebsite(newWebsite: String, onError: () -> Unit) {
        if (isSuccess) {
            website = newWebsite
        } else {
            onError()
        }
    }

    override fun currentUserResidence(): String = residence

    override fun saveUserResidence(newResidence: String, onError: () -> Unit) {
        if (isSuccess) {
            residence = newResidence
        } else {
            onError()
        }
    }

    override fun currentUserLanguage(): String = language

    override fun saveUserLanguage(newLanguage: String, onError: () -> Unit) {
        if (isSuccess) {
            language = newLanguage
        } else {
            onError()
        }
    }

    override fun currentUserEducation(): String = education

    override fun saveUserEducation(newEducation: String, onError: () -> Unit) {
        if (isSuccess) {
            education = newEducation
        } else {
            onError()
        }
    }

    override fun currentUserWork(): String = work

    override fun saveUserWork(newWork: String, onError: () -> Unit) {
        if (isSuccess) {
            work = newWork
        } else {
            onError()
        }
    }

    override fun currentUserInterests(): String = interests

    override fun saveUserInterests(newInterests: String, onError: () -> Unit) {
        if (isSuccess) {
            interests = newInterests
        } else {
            onError()
        }
    }

    override fun startGettingConnectionStateUpdates(onChange: (Boolean) -> Unit) {
    }

    override fun stopGettingConnectionStateUpdates() {
    }

    override fun setUserOnline() {
    }

    override fun setUserOffline() {
    }

    override fun currentUserStatus(): String = status

    override fun saveUserStatus(newStatus: String, onError: () -> Unit) {
        if (isSuccess) {
            status = newStatus
        } else {
            onError()
        }
    }

    override fun saveUserActivity(newActivity: Long) {
        if (isSuccess) {
            activity = newActivity
        }
    }

    override fun startGettingSecondUserReviewsUpdates(uid: String) {
    }

    override fun stopGettingSecondUserReviewsUpdates() {
    }

    override fun saveAwardCongratulationShown(newAwardCongratulationShownList: MutableList<Int>) {
        if (isSuccess) awardCongratulationShownList.addAll(newAwardCongratulationShownList)
    }
}