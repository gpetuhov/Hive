package com.gpetuhov.android.hive.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

fun Fragment.showMainHeader(onNavigationClick: () -> Unit, onClick: () -> Unit) {
    initMainHeader(true, onNavigationClick, onClick)
}

fun Fragment.hideMainHeader() {
    initMainHeader(false, { /* Do nothing */ }, { /* Do nothing */ })
}

fun Fragment.setMainHeaderUserPic(user: User) {
    updateUserPic(this, user, getMainHeaderImage())
}

fun Fragment.setMainHeaderTitle(title: String) {
    getMainHeaderTitle()?.text = title
}

fun Fragment.setMainHeaderOnlineAndLastSeen(isOnline: Boolean, lastSeen: String) {
    getMainHeaderOnline()?.setVisible(isOnline)
    getMainHeaderLastSeen()?.setVisible(!isOnline)

    val lastSeenPrefix = context?.getString(R.string.last_seen) ?: ""
    val lastSeenText = "$lastSeenPrefix $lastSeen"
    getMainHeaderLastSeen()?.text = lastSeenText
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

private fun Fragment.getMainHeader() = activity?.findViewById<View>(R.id.header)

private fun Fragment.getMainHeaderImage() = activity?.findViewById(R.id.header_image) ?: ImageView(context)

private fun Fragment.getMainHeaderTitle() = activity?.findViewById<TextView>(R.id.header_title)

private fun Fragment.getMainHeaderOnline() = activity?.findViewById<TextView>(R.id.header_online)

private fun Fragment.getMainHeaderLastSeen() = activity?.findViewById<TextView>(R.id.header_last_seen)

private fun Fragment.initMainHeader(isVisible: Boolean, onNavigationClick: () -> Unit, onClick: () -> Unit) {
    val mainHeader = getMainHeader()
    mainHeader?.setVisible(isVisible)
    mainHeader?.setOnClickListener { onClick() }

    val backButton = mainHeader?.findViewById<ImageButton>(R.id.header_back_button)
    backButton?.setOnClickListener { onNavigationClick() }
}
