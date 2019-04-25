package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ReviewsAllViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val allReviews = MutableLiveData<MutableList<Review>>()

    private var hasData = false

    init {
        HiveApp.appComponent.inject(this)
    }

    fun getAllReviews(isCurrentUser: Boolean) {
        if (!hasData) {
            repo.getAllUserReviews(isCurrentUser) { reviewList ->
                hasData = true
                allReviews.value = reviewList
            }
        }
    }
}