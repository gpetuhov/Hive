package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentFavoritesBinding
import com.gpetuhov.android.hive.presentation.presenter.FavoritesFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.FavoritesFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class FavoritesFragment : BaseFragment(), FavoritesFragmentView {

    @InjectPresenter lateinit var presenter: FavoritesFragmentPresenter

    private var binding: FragmentFavoritesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding?.root
    }
}