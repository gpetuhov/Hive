package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ReviewsAllViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val allReviews = MutableLiveData<MutableList<Review>>()
    var user: User? = null

    private var hasData = false

    init {
        HiveApp.appComponent.inject(this)
    }

    fun getAllReviews(isCurrentUser: Boolean) {
        if (!hasData) {
            hasData = true

            user = if (isCurrentUser) repo.currentUser().value else repo.secondUser().value

            repo.getAllUserReviews(isCurrentUser) { reviewList ->
                allReviews.value = reviewList
            }
        }
    }
}