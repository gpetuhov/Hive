package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.presentation.presenter.CongratulationFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.CongratulationFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class CongratulationFragment : BaseFragment(), CongratulationFragmentView {

    @InjectPresenter lateinit var presenter: CongratulationFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        return inflater.inflate(R.layout.fragment_congratulation, container, false)
    }
}