package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.update.controller.UpdateOfferListController
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class UpdateOfferFragment : MvpAppCompatFragment(), UpdateOfferFragmentView {

    @InjectPresenter lateinit var presenter: UpdateOfferFragmentPresenter

    private lateinit var controller: UpdateOfferListController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        val view = inflater.inflate(R.layout.fragment_update_offer, container, false)

        controller = UpdateOfferListController(presenter)

        val updateOfferRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.update_offer_recycler_view)
        updateOfferRecyclerView.adapter = controller.adapter

        // TODO: remove this
        controller.requestModelBuild()

        return view
    }
}