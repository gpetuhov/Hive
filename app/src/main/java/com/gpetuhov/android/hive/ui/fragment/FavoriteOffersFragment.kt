package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentFavoriteOffersBinding
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.presentation.presenter.FavoriteOffersFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.FavoriteOffersFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.favorite.controller.OfferFavoriteListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.FavoriteOffersViewModel

class FavoriteOffersFragment : BaseFragment(), FavoriteOffersFragmentView {

    @InjectPresenter lateinit var presenter: FavoriteOffersFragmentPresenter

    private var controller: OfferFavoriteListController? = null
    private var binding: FragmentFavoriteOffersBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        controller = OfferFavoriteListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_offers, container, false)

        val favoriteOffersRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.favorite_offers_recycler_view)
        favoriteOffersRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(FavoriteOffersViewModel::class.java)
        viewModel.favoriteOffers.removeObservers(this)
        viewModel.favoriteOffers.observe(this, Observer<MutableList<Offer>> { favoriteOffersList ->
            binding?.favoriteOffersListEmpty = favoriteOffersList.isEmpty()
            controller?.changeFavoriteOffersList(favoriteOffersList)
        })

        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === FavoriteOffersFragmentView ===

    override fun showOfferDetails(offerUid: String) {
        val action = FavoriteOffersFragmentDirections.actionGlobalOfferDetailsFragment(offerUid)
        findNavController().navigate(action)
    }
}