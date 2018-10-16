package com.gpetuhov.android.hive.ui.activity

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gpetuhov.android.hive.R
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_permissions.*
import org.jetbrains.anko.toast
import timber.log.Timber

class PermissionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PermissionsActivity"
        private const val PERMISSION_DIALOG_SHOWING_KEY = "permissionDialogShowing"
        private const val PERM_REQUEST_CODE = 0
        private const val PERM_SYSTEM_SETTINGS_REQUEST_CODE = 1
    }

    private var isPermissionDialogShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkHasPermissions()) {
            closePermissionsScreen()
        } else {
            setContentView(R.layout.activity_permissions)

            if (savedInstanceState != null) {
                isPermissionDialogShowing = savedInstanceState.getBoolean(PERMISSION_DIALOG_SHOWING_KEY, false)
            }

            grantPermissionsButton.setOnClickListener { requestPermissions() }
        }
    }

    // Called, when permissions request response received
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.tag(TAG).d("onRequestPermissionsResult")

        if (requestCode == PERM_REQUEST_CODE) {
            isPermissionDialogShowing = false

            var isAllGranted = true

            if (grantResults.isEmpty()) {
                isAllGranted = false
            }

            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false
                    break
                }
            }

            if (!isAllGranted) {
                if (shouldShowRationale()) {
                    // If rationale should be shown, request permissions again
                    requestPermissions()
                } else {
                    // Rationale should not be shown (this means, user denied permission and opted Don't show again).
                    // Explicitly open system settings, so that user could grant permissions.
                    openSystemSettings()
                }
            } else {
                closePermissionsScreenAfterGranted()
            }
        }
    }

    // We get here from system settings
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.tag(TAG).d("onActivityResult")

        if (requestCode == PERM_SYSTEM_SETTINGS_REQUEST_CODE) {
            if (!checkHasPermissions()) {
                openSystemSettings()
            } else {
                closePermissionsScreenAfterGranted()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.tag(TAG).d("onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putBoolean(PERMISSION_DIALOG_SHOWING_KEY, isPermissionDialogShowing)
    }

    private fun checkHasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (!isPermissionDialogShowing) {
            isPermissionDialogShowing = true

            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            ActivityCompat.requestPermissions(this, permissions, PERM_REQUEST_CODE)
            // Result is passed to onRequestPermissionsResult() method
        }
    }

    private fun shouldShowRationale(): Boolean {
        // False if the app runs for the first time or the user denied permissions and opted Don't show again
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun openSystemSettings() {
        toast(getString(R.string.permissions_warning2))

        // Open system settings
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        startActivityForResult(intent, PERM_SYSTEM_SETTINGS_REQUEST_CODE)

        // Result will be passed to onActivityResult() method
    }

    private fun closePermissionsScreen() {
        // Here we start main activity first, so that previously shown splash screen won't animate
        startActivity<MainActivity>()
        finish()
    }

    private fun closePermissionsScreenAfterGranted() {
        // This is needed to close permissions screen immediately after permissions have been granted
        finish()
        startActivity<MainActivity>()
    }
}