package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class FavoritesInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onFavoritesError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var isFavorite = false
    private var userUid = ""
    private var offerUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        if (isFavorite) {
            // If user or offer is currently favorite, remove from favorites
            repo.removeFavorite(userUid, "") { callback.onFavoritesError(resultMessages.getRemoveFavoriteErrorMessage()) }
        } else {
            // Otherwise, add to favorites
            repo.addFavorite(userUid, "") { callback.onFavoritesError(resultMessages.getAddFavoriteErrorMessage()) }
        }
    }

    // Call this method to add or remove favorite
    fun favorite(isFavorite: Boolean, userUid: String, offerUid: String) {
        this.isFavorite = isFavorite
        this.userUid = userUid
        this.offerUid = offerUid
        execute()
    }
}