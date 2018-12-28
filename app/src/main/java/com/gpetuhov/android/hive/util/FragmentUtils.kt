package com.gpetuhov.android.hive.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.User

// === Public methods ===

fun Fragment.setActivitySoftInputResize() = setActivitySoftInput(true)

fun Fragment.setActivitySoftInputPan() = setActivitySoftInput(false)

fun Fragment.hideSoftKeyboard() {
    if (activity != null) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

fun Fragment.showBottomNavigationView() {
    getBottomNavigationView()?.visibility = View.VISIBLE
}

fun Fragment.hideBottomNavigationView() {
    getBottomNavigationView()?.visibility = View.GONE
}

fun Fragment.showToolbar(onNavigationClick: () -> Unit, onClick: () -> Unit) {
    initToolbar(true, onNavigationClick, onClick)
}

fun Fragment.hideToolbar() {
    initToolbar(false, { /* Do nothing */ }, { /* Do nothing */ })
}

fun Fragment.setToolbarUserPic(user: User) {
    updateUserPic(this, user, getToolbarImage())
}

fun Fragment.setToolbarTitle(title: String) {
    getToolbarTitle()?.text = title
}

fun Fragment.startPhotoPicker(requestCode: Int) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = Constants.FileTypes.IMAGE
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
    startActivityForResult(Intent.createChooser(intent, getString(R.string.complete_action_using)), requestCode)
    // Result will be passed into onActivityResult()
}

// === Private methods ===

private fun Fragment.setActivitySoftInput(isResize: Boolean) {
    val resizeOrPan =
        if (isResize) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED or resizeOrPan)
}

private fun Fragment.getBottomNavigationView() = activity?.findViewById<BottomNavigationView>(R.id.navigation_view)

private fun Fragment.getToolbar() = activity?.findViewById<View>(R.id.toolbar)

private fun Fragment.getToolbarImage() = activity?.findViewById(R.id.toolbar_image) ?: ImageView(context)

private fun Fragment.getToolbarTitle() = activity?.findViewById<TextView>(R.id.toolbar_title)

private fun Fragment.initToolbar(isVisible: Boolean, onNavigationClick: () -> Unit, onClick: () -> Unit) {
    val toolbar = getToolbar()

    toolbar?.setVisible(isVisible)

    val backButton = toolbar?.findViewById<ImageButton>(R.id.toolbar_back_button)
    backButton?.setOnClickListener { onNavigationClick() }
    toolbar?.setOnClickListener { onClick() }
}
