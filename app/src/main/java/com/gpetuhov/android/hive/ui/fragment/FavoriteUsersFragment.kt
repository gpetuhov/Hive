package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentFavoriteUsersBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.FavoriteUsersFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.FavoriteUsersFragmentView
import com.gpetuhov.android.hive.ui.epoxy.user.favorite.controller.UserFavoriteListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.FavoriteUsersViewModel

class FavoriteUsersFragment : BaseFragment(), FavoriteUsersFragmentView {

    @InjectPresenter lateinit var presenter: FavoriteUsersFragmentPresenter

    private var controller: UserFavoriteListController? = null
    private var binding: FragmentFavoriteUsersBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        controller = UserFavoriteListController()
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_users, container, false)

        val favoriteUsersRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.favorite_users_recycler_view)
        favoriteUsersRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(FavoriteUsersViewModel::class.java)
        viewModel.favoriteUsers.observe(this, Observer<MutableList<User>> { favoriteUsersList ->
            binding?.favoriteUsersListEmpty = favoriteUsersList.isEmpty()
            controller?.changeFavoriteUsersList(favoriteUsersList)
        })

        return binding?.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }
}