package com.gpetuhov.android.hive.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.domain.model.User

// At domain layer (business logic layer) we have only interface of the repo.
// All classes at this layer call methods of this interface to retrieve and save data.
// The interface must be implemented at the storage (outer) layer.
interface Repo {
    fun onSignIn(user: User)
    fun onSignOut()
    fun currentUser(): MutableLiveData<User>
    fun currentUserUsername(): String
    fun currentUserService(): String
    fun saveUserVisibility(newIsVisible: Boolean, onError: () -> Unit)
    fun saveUserUsername(newUsername: String, onError: () -> Unit)
    fun saveUserService(newService: String, onError: () -> Unit)
    fun deleteUserService(onError: () -> Unit)
    fun saveUserLocation(newLocation: LatLng)
    fun saveUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit)
    fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit)
    fun searchResult(): MutableLiveData<MutableMap<String, User>>
    fun search(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String, onComplete: () -> Unit)
    fun stopGettingSearchResultUpdates()
    fun searchUserDetails(): MutableLiveData<User>
    fun initSearchUserDetails(uid: String)
    fun startGettingSearchUserDetailsUpdates(uid: String)
    fun stopGettingSearchUserDetailsUpdates()
}