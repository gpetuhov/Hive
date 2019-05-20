package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ReviewsViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val reviews: LiveData<MutableList<Review>>
    val secondUser: LiveData<User>

    init {
        HiveApp.appComponent.inject(this)
        reviews = repo.reviews()
        secondUser = repo.secondUser()
    }
}