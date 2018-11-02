package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentDetailsBinding
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class DetailsFragment : Fragment() {

    @Inject lateinit var repo: Repo

    private var binding: FragmentDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding?.handler = this

        val uid = DetailsFragmentArgs.fromBundle(arguments).uid
        val user = repo.searchResult().value?.get(uid)
        if (user != null) binding?.user = user

        return binding?.root
    }

    fun closeDetails() = findNavController().navigateUp()
}