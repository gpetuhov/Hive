package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.update.controller.UpdateOfferListController
import com.gpetuhov.android.hive.util.hideSoftKeyboard
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import com.pawegio.kandroid.toast

class UpdateOfferFragment : MvpAppCompatFragment(), UpdateOfferFragmentView {

    @InjectPresenter lateinit var presenter: UpdateOfferFragmentPresenter

    private var controller: UpdateOfferListController? = null

    private var titleDialog: MaterialDialog? = null
    private var descriptionDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        initDialogs()

        presenter.initOffer(UpdateOfferFragmentArgs.fromBundle(arguments).offerUid)

        val view = inflater.inflate(R.layout.fragment_update_offer, container, false)

        controller = UpdateOfferListController(presenter)

        val updateOfferRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.update_offer_recycler_view)
        updateOfferRecyclerView.adapter = controller?.adapter

        updateUI()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // This is needed to prevent memory leaks.
        // Here we intentially dismiss dialogs directly, not via the presenter,
        // so that Moxy command queue doesn't change.
        dismissDialogs()
    }

    // === UpdateOfferFragmentView ===

    override fun showTitleDialog() {
        // Prefill dialog with text provided by presenter
        val editText = titleDialog?.getInputField()
        editText?.setText(presenter.getTitlePrefill())
        editText?.setSelection(editText.text.length)
        titleDialog?.show()
    }

    override fun dismissTitleDialog() = titleDialog?.dismiss() ?: Unit

    override fun showDescriptionDialog() {
        // Prefill dialog with text provided by presenter
        val editText = descriptionDialog?.getInputField()
        editText?.setText(presenter.getDescriptionPrefill())
        editText?.setSelection(editText.text.length)
        descriptionDialog?.show()
    }

    override fun dismissDescriptionDialog() = descriptionDialog?.dismiss() ?: Unit

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
        hideSoftKeyboard()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

    private fun initDialogs() {
        initTitleDialog()
        initDescriptionDialog()
    }

    private fun initTitleDialog() {
        if (context != null) {
            titleDialog = MaterialDialog(context!!)
                .title(R.string.offer_title)
                .noAutoDismiss()
                .cancelable(false)
                .input(hintRes = R.string.add_title, waitForPositiveButton = false) { dialog, text ->
                    presenter.updateTempTitle(text.toString())
                }
                .positiveButton { presenter.saveTitle() }
                .negativeButton { presenter.dismissTitleDialog() }
        }
    }

    private fun initDescriptionDialog() {
        if (context != null) {
            descriptionDialog = MaterialDialog(context!!)
                .title(R.string.offer_description)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                    hintRes = R.string.add_description,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    presenter.updateTempDescription(text.toString())
                }
                .positiveButton { presenter.saveDescription() }
                .negativeButton { presenter.dismissDescriptionDialog() }
        }
    }

    private fun dismissDialogs() {
        dismissTitleDialog()
        dismissDescriptionDialog()
    }
}