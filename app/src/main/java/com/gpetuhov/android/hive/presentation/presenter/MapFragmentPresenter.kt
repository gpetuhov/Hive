package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import javax.inject.Inject

@InjectViewState
class MapFragmentPresenter : MvpPresenter<MapFragmentView>() {

    @Inject lateinit var mapManager: MapManager
    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    fun moveToCurrentLocation() = mapManager.moveToCurrentLocation()

    fun zoomIn() = mapManager.zoomIn()

    fun zoomOut() = mapManager.zoomOut()
}