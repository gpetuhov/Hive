package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment

class FavoriteOffersFragment : BaseFragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_offers, container, false)
    }
}