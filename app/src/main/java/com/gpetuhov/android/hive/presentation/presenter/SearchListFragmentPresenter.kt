package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.FavoritesInteractor
import com.gpetuhov.android.hive.domain.interactor.SearchInteractor
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.SearchListFragmentView
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class SearchListFragmentPresenter :
    MvpPresenter<SearchListFragmentView>(),
    SearchInteractor.Callback,
    FavoritesInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var settings: Settings

    var queryLatitude = Constants.Map.DEFAULT_LATITUDE
    var queryLongitude = Constants.Map.DEFAULT_LONGITUDE
    var queryRadius = Constants.Map.DEFAULT_RADIUS
    var queryText = ""

    var searchResultList = mutableListOf<User>()

    // Keeps current text entered in search dialog
    private var tempQueryText = ""

    private val searchInteractor = SearchInteractor(this)
    private var favoritesInteractor = FavoritesInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SearchInteractor.Callback ===

    override fun onSearchComplete() = viewState.onSearchComplete()

    // === FavoritesInteractor.Callback ===

    override fun onFavoritesError(errorMessage: String) = viewState.showToast(errorMessage)

    // === Public methods ===

    fun initSearchQueryText() {
        queryText = settings.getSearchQueryText() ?: ""
    }

    fun navigateUp() = viewState.navigateUp()

    fun onResume() {
        repo.setSearchListActive(true)
        search()
    }

    fun onPause() {
        repo.stopGettingSearchResultUpdates()
        repo.setSearchListActive(false)
    }

    fun updateSearchResult(searchResult: MutableMap<String, User>) {
        sortSearchResultList(searchResult.values.toMutableList()) { sortedList ->
            searchResultList.clear()
            searchResultList.addAll(sortedList)
            viewState.updateUI()
        }
    }

    fun showDetails(userUid: String, offerUid: String) {
        // This is needed to get user details immediately from the already available search results
        repo.initSearchUserDetails(userUid)
        viewState.showDetails(offerUid)
    }

    fun favorite(isFavorite: Boolean, userUid: String, offerUid: String) =
        favoritesInteractor.favorite(isFavorite, userUid, offerUid)

    fun filterIsDefault() = settings.getSearchFilter().isDefault

    fun sortIsDefault() = settings.getSearchSort().isDefault

    fun showFilter() = viewState.showFilter()
    
    fun showSort() = viewState.showSort()

    // --- Search dialog ---

    fun showSearchDialog() = viewState.showSearchDialog()

    // Prefill search dialog with currently entered text or current value
    fun getSearchPrefill() = if (tempQueryText != "") tempQueryText else queryText

    fun updateTempQueryText(newQueryText: String) {
        tempQueryText = newQueryText
    }

    fun startSearch() {
        queryText = tempQueryText
        dismissSearchDialog()
        search()
    }

    fun dismissSearchDialog() {
        tempQueryText = ""
        viewState.dismissSearchDialog()
    }

    // === Private methods ===

    private fun search() {
        viewState.onSearchStart()
        settings.setSearchQueryText(queryText)
        searchInteractor.search(queryLatitude, queryLongitude, queryRadius, queryText)
    }

    private fun sortSearchResultList(unsortedList: MutableList<User>, onComplete: (MutableList<User>) -> Unit) {
        GlobalScope.launch {
            val sortedList = mutableListOf<User>()
            val userList = mutableListOf<User>()
            val offerList = mutableListOf<User>()

            // Separate users and offers into different lists
            unsortedList.forEach { if (it.offerSearchResultIndex == -1) userList.add(it) else offerList.add(it) }

            val sort = settings.getSearchSort()

            // By default sort users by name and offers by title
            offerList.sortBy {
                // TODO: add sort by price and rating
                if (sort.isSortByTitle) it.getSearchedOffer()?.title
                else it.getSearchedOffer()?.title
            }

            userList.sortBy {
                // TODO: add sort by rating
                // Users can by sorted only by name and rating and cannot be sorted by price
                it.getUsernameOrName()
            }

            // TODO: this should change according to user selected options
            sortedList.addAll(offerList)
            sortedList.addAll(userList)

            launch(Dispatchers.Main) { onComplete(sortedList) }
        }
    }
}