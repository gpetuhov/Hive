package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentAwardBinding
import com.gpetuhov.android.hive.presentation.presenter.AwardFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.AwardFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import kotlinx.android.synthetic.main.fragment_award.*

class AwardFragment : BaseFragment(), AwardFragmentView {

    @InjectPresenter lateinit var presenter: AwardFragmentPresenter

    private var binding: FragmentAwardBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_award, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = AwardFragmentArgs.fromBundle(arguments!!)
        val awardType = args.awardType

        updateUI(awardType)
    }

    // === AwardFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun updateUI(awardType: Int) {
        val awardAnimationId: Int
        val awardImageId: Int
        val awardNameId: Int
        val awardDescriptionId: Int

        when (awardType) {
            Constants.Award.TEXT_MASTER -> {
                awardAnimationId = R.raw.textmaster
                awardImageId = R.drawable.ic_text_master_big
                awardNameId = R.string.text_master
                awardDescriptionId = R.string.text_master_info
            }
            else -> {
                awardAnimationId = R.raw.gears
                awardImageId = R.drawable.ic_text_master_big
                awardNameId = R.string.info
                awardDescriptionId = R.string.info
            }
        }

        award_animation.setAnimation(awardAnimationId)
        award_image.setImageResource(awardImageId)
        binding?.awardNameText = getString(awardNameId)
        binding?.awardDescriptionText = getString(awardDescriptionId)
    }
}