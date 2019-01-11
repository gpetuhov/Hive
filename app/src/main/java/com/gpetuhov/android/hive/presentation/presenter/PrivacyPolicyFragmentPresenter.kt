package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.PrivacyPolicyFragmentView

@InjectViewState
class PrivacyPolicyFragmentPresenter : MvpPresenter<PrivacyPolicyFragmentView>() {

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()
}