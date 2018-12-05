package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.update.controller.UpdateOfferListController
import com.gpetuhov.android.hive.util.*
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_update_offer.*

class UpdateOfferFragment : BaseFragment(), UpdateOfferFragmentView {

    @InjectPresenter lateinit var presenter: UpdateOfferFragmentPresenter

    private var controller: UpdateOfferListController? = null

    private var titleDialog: MaterialDialog? = null
    private var descriptionDialog: MaterialDialog? = null
    private var deleteOfferDialog: MaterialDialog? = null
    private var quitDialog: MaterialDialog? = null
    private var priceDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()

        // Hide bottom navigation view, so that user can't quit without quit dialog
        hideBottomNavigationView()

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

    override fun onBackPressed(): Boolean {
        presenter.showQuitOfferUpdateDialog()
        return true
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

    override fun disableButtons() = saveDeleteButtonsEnabled(false)

    override fun enableButtons() = saveDeleteButtonsEnabled(true)

    override fun showProgress() = progressVisible(true)

    override fun hideProgress() = progressVisible(false)

    override fun showDeleteOfferDialog() = deleteOfferDialog?.show() ?: Unit

    override fun dismissDeleteOfferDialog() = deleteOfferDialog?.dismiss() ?: Unit

    override fun showQuitOfferUpdateDialog() = quitDialog?.show() ?: Unit

    override fun dismissQuitOfferUpdateDialog() = quitDialog?.dismiss() ?: Unit

    override fun showPriceDialog() {
        // Prefill dialog with text provided by presenter
        val editText = priceDialog?.getInputField()
        editText?.setText(presenter.getPricePrefill())
        editText?.setSelection(editText.text.length)
        priceDialog?.show()
    }

    override fun dismissPriceDialog() = priceDialog?.dismiss() ?: Unit

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
        initDeleteOfferDialog()
        initQuitDialog()
        initPriceDialog()
    }

    private fun initTitleDialog() {
        if (context != null) {
            titleDialog = MaterialDialog(context!!)
                .title(R.string.offer_title)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = Constants.Offer.MAX_TITLE_LENGTH,
                    hintRes = R.string.add_title,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    val positiveButtonEnabled = inputText.length <= Constants.Offer.MAX_TITLE_LENGTH
                    presenter.updateTempTitle(inputText)
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
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
                    maxLength = Constants.Offer.MAX_DESCRIPTION_LENGTH,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                    hintRes = R.string.add_description,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    val positiveButtonEnabled = inputText.length <= Constants.Offer.MAX_DESCRIPTION_LENGTH
                    presenter.updateTempDescription(inputText)
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.saveDescription() }
                .negativeButton { presenter.dismissDescriptionDialog() }
        }
    }

    private fun initDeleteOfferDialog() {
        if (context != null) {
            deleteOfferDialog = MaterialDialog(context!!)
                .title(R.string.delete_offer)
                .message(R.string.prompt_delete_offer)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deleteOffer() }
                .negativeButton { presenter.deleteOfferCancel() }
        }
    }

    private fun initQuitDialog() {
        if (context != null) {
            quitDialog = MaterialDialog(context!!)
                .title(R.string.quit)
                .message(R.string.quit_offer_update_prompt)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.quitOfferUpdate() }
                .negativeButton { presenter.quitOfferUpdateCancel() }
        }
    }

    private fun initPriceDialog() {
        if (context != null) {
            priceDialog = MaterialDialog(context!!)
                .title(R.string.offer_price)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                    hintRes = R.string.enter_price,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputField = dialog.getInputField()

                    var positiveButtonEnabled: Boolean
                    var error: String? = null

                    val isEmpty = text.toString().isEmpty()

                    if (!isEmpty) {
                        try {
                            val number = text.toString().toDouble()

                            if (number >= 0.01) {
                                presenter.updateTempPrice(number)
                                positiveButtonEnabled = true
                            } else {
                                error = "Must not be less than 0.01"
                                positiveButtonEnabled = false
                            }

                        } catch (e: Exception) {
                            error = "Not a number"
                            positiveButtonEnabled = false
                        }

                    } else {
                        positiveButtonEnabled = false
                    }

                    inputField?.error = error
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.savePrice() }
                .negativeButton { presenter.dismissPriceDialog() }
        }
    }

    private fun dismissDialogs() {
        dismissTitleDialog()
        dismissDescriptionDialog()
        dismissDeleteOfferDialog()
        dismissQuitOfferUpdateDialog()
        dismissPriceDialog()
    }

    private fun saveDeleteButtonsEnabled(isEnabled: Boolean) {
        controller?.saveButtonEnabled(isEnabled)
        controller?.deleteButtonEnabled(isEnabled)
    }

    private fun progressVisible(isVisible: Boolean) {
        update_offer_progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}