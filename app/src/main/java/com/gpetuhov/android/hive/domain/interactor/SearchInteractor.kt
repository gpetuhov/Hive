package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.util.Constants
import javax.inject.Inject

class SearchInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSearchComplete()
    }

    @Inject lateinit var repo: Repo

    private var queryLatitude = Constants.Map.DEFAULT_LATITUDE
    private var queryLongitude = Constants.Map.DEFAULT_LONGITUDE
    private var queryRadius = Constants.Map.DEFAULT_RADIUS
    private var queryText = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() = repo.search(queryLatitude, queryLongitude, queryRadius, queryText) { callback.onSearchComplete() }

    // Call this method to perform search
    fun search(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String) {
        this.queryLatitude = queryLatitude
        this.queryLongitude = queryLongitude
        this.queryRadius = queryRadius
        this.queryText = queryText
        execute()
    }
}