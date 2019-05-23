package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentCongratulationBinding
import com.gpetuhov.android.hive.presentation.presenter.CongratulationFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.CongratulationFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import kotlinx.android.synthetic.main.fragment_congratulation.*

class CongratulationFragment : BaseFragment(), CongratulationFragmentView {

    companion object {
        const val NEW_AWARD_LIST_KEY = "newAwardList"
    }

    @InjectPresenter lateinit var presenter: CongratulationFragmentPresenter

    private var binding: FragmentCongratulationBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_congratulation, container, false)
        binding?.presenter = presenter

        val args = CongratulationFragmentArgs.fromBundle(arguments!!)
        val newAwardsBundle = args.newAwardsBundle
        val newAwardsList = newAwardsBundle.getIntegerArrayList(NEW_AWARD_LIST_KEY)?.toMutableList() ?: mutableListOf()
        presenter.saveAwardCongratulationShown(newAwardsList)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This is needed to disable award list scrolling, because it is inside ScrollView
        congratulation_award_list.isNestedScrollingEnabled = false
    }

    // === CongratulationFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}