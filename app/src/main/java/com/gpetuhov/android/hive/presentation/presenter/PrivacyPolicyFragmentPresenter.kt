package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.PrivacyPolicyFragmentView

class PrivacyPolicyFragmentPresenter : MvpPresenter<PrivacyPolicyFragmentView>() {

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()
}