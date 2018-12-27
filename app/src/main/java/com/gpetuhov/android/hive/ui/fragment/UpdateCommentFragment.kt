package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUpdateCommentBinding
import com.gpetuhov.android.hive.presentation.presenter.UpdateCommentFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateCommentFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.*
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_update_comment.*

class UpdateCommentFragment : BaseFragment(), UpdateCommentFragmentView {

    @InjectPresenter lateinit var presenter: UpdateCommentFragmentPresenter

    private var binding: FragmentUpdateCommentBinding? = null

    private var quitDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        hideBottomNavigationView()

        initDialogs()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_comment, container, false)
        binding?.presenter = presenter

        // TODO: get args
//        val args = UpdateCommentFragmentArgs.fromBundle(arguments)
//        presenter.init(args.offerUid, args.reviewUid, args.reviewText, args.rating )

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissDialogs()
    }

    override fun onBackPressed(): Boolean {
        presenter.showQuitCommentUpdateDialog()
        return true
    }

    // === UpdateCommentFragmentView ===

    override fun disableButtons() = saveButtonEnabled(false)

    override fun enableButtons() = saveButtonEnabled(true)

    override fun showProgress() = progressVisible(true)

    override fun hideProgress() = progressVisible(false)

    override fun showQuitCommentUpdateDialog() = quitDialog?.show() ?: Unit

    override fun dismissQuitCommentUpdateDialog() = quitDialog?.dismiss() ?: Unit

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateUp() {
        findNavController().navigateUp()
        hideSoftKeyboard()
    }

    // === Private methods ===

    private fun initDialogs() {
        initQuitDialog()
    }

    private fun initQuitDialog() {
        if (context != null) {
            quitDialog = MaterialDialog(context!!)
                .title(R.string.quit)
                .message(R.string.quit_review_update_prompt)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.quitCommentUpdate() }
                .negativeButton { presenter.quitCommentUpdateCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissQuitCommentUpdateDialog()
    }

    private fun saveButtonEnabled(isEnabled: Boolean) {
        update_comment_save_button.isEnabled = isEnabled
    }

    private fun progressVisible(isVisible: Boolean) = update_comment_progress.setVisible(isVisible)
}