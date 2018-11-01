package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class SearchInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSearchComplete()
    }

    @Inject lateinit var repo: Repo

    private var queryText = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() = repo.search(queryText)

    // Call this method to perform search
    fun search(queryText: String) {
        this.queryText = queryText
        execute()
    }
}