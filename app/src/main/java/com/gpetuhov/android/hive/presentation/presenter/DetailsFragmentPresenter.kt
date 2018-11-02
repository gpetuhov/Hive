package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.DetailsFragmentView
import javax.inject.Inject

class DetailsFragmentPresenter : MvpPresenter<DetailsFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

}