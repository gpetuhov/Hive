package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentFavoriteOffersBinding
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.presentation.presenter.FavoriteOffersFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.FavoriteOffersFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.FavoriteOffersViewModel

class FavoriteOffersFragment : BaseFragment(), FavoriteOffersFragmentView {

    @InjectPresenter lateinit var presenter: FavoriteOffersFragmentPresenter

    private var binding: FragmentFavoriteOffersBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_offers, container, false)

        val viewModel = ViewModelProviders.of(this).get(FavoriteOffersViewModel::class.java)
        viewModel.favoriteOffers.observe(this, Observer<MutableList<Offer>> { favoriteOffersList ->
            binding?.favoriteOffersListEmpty = favoriteOffersList.isEmpty()
        })

        return binding?.root
    }
}