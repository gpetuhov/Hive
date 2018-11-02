package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentDetailsBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.ui.viewmodel.SearchUserDetailsViewModel
import javax.inject.Inject

// Shows user details on map marker click
class DetailsFragment : Fragment() {

    @Inject lateinit var repo: Repo

    private var binding: FragmentDetailsBinding? = null
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding?.handler = this

        val viewModel = ViewModelProviders.of(this).get(SearchUserDetailsViewModel::class.java)

        uid = DetailsFragmentArgs.fromBundle(arguments).uid

        // This is needed to get user details immediately from the already available search results
        viewModel.getFirstUpdate(uid)

        viewModel.searchUserDetails.observe(this, Observer<User> { user ->
            binding?.user = user
        })

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        // This is needed to change user details in the UI if changed on the backend
        repo.startGettingSearchUserDetailsUpdates(uid)
    }

    override fun onPause() {
        super.onPause()
        repo.stopGettingSearchUserDetailsUpdates()
    }

    fun closeDetails() = findNavController().navigateUp()
}