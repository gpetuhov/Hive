package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentFavoriteUsersBinding
import com.gpetuhov.android.hive.domain.model.Favorite
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.FavoritesViewModel

class FavoriteUsersFragment : BaseFragment() {

    private var binding: FragmentFavoriteUsersBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_users, container, false)

        val viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        viewModel.favorites.observe(this, Observer<MutableList<Favorite>> { favoritesList ->
            val favoriteUsersList = favoritesList.filter { !it.isOffer() }
            binding?.favoriteUsersListEmpty = favoriteUsersList.isEmpty()
        })

        return binding?.root
    }
}