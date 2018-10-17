package com.gpetuhov.android.hive.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.util.checkPermissions
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 101
    }

    @Inject
    lateinit var locationManager: LocationManager

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HiveApp.appComponent.inject(this)

        // Find NavController
        navController = findNavController(R.id.nav_host)

        // Tie NavHostFragment to bottom navigation bar
        navigation_view.setupWithNavController(navController)

        locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()

        if (checkPlayServices()) {
            if (!checkPermissions(this)) {
                startActivity<PermissionsActivity>()
                finish()
            }
        }

        locationManager.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationManager.stopLocationUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != Activity.RESULT_OK) {
                toast("Please, turn on geolocation")
                locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)
            }
        }
    }

    // Check if Google Play Services installed
    private fun checkPlayServices(): Boolean {
        var servicesAvailable = true
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            servicesAvailable = false
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, 1).show()
            } else {
                longToast(R.string.play_services_unavailable)
                finish()
            }
        }

        return servicesAvailable
    }
}
