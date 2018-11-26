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
import androidx.databinding.DataBindingUtil
import com.gpetuhov.android.hive.databinding.ActivityPermissionsBinding
import com.gpetuhov.android.hive.util.checkPermissions
import com.pawegio.kandroid.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber

// Check if all permissions are granted.
// If yes, just finish.
class PermissionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PermissionsActivity"
        private const val PERM_REQUEST_CODE = 0
        private const val PERM_SYSTEM_SETTINGS_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkHasPermissions()) {
            closePermissionsScreen()
        } else {
            val binding: ActivityPermissionsBinding = DataBindingUtil.setContentView(this, R.layout.activity_permissions)
            binding.handler = this
        }
    }

    // Called, when permissions request response received
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.tag(TAG).d("onRequestPermissionsResult")

        if (requestCode == PERM_REQUEST_CODE) {
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

    fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, PERM_REQUEST_CODE)
        // Result is passed to onRequestPermissionsResult() method
    }

    private fun checkHasPermissions() = checkPermissions(this)

    private fun shouldShowRationale(): Boolean {
        // False if the app runs for the first time or the user denied permissions and opted Don't show again
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun openSystemSettings() {
        toast(getString(R.string.permissions_toast))

        // Open system settings
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        startActivityForResult(intent, PERM_SYSTEM_SETTINGS_REQUEST_CODE)

        // Result will be passed to onActivityResult() method
    }

    private fun closePermissionsScreen() {
        // Here we start main activity first, so that previously shown splash screen won't animate
        startActivity<AuthActivity>()
        finish()
    }

    private fun closePermissionsScreenAfterGranted() {
        // This is needed to close permissions screen immediately after permissions have been granted
        finish()
        startActivity<AuthActivity>()
    }
}