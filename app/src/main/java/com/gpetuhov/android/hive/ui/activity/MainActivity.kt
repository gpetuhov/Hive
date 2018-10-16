package com.gpetuhov.android.hive.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.gpetuhov.android.hive.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find NavController
        navController = findNavController(R.id.nav_host)

        // Tie NavHostFragment to bottom navigation bar
        navigation_view.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()
        checkPlayServices()
    }

    // Check if Google Play Services installed
    private fun checkPlayServices() {
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (gApi.isUserResolvableError(resultCode)) {
                gApi.getErrorDialog(this, resultCode, 1).show()
            } else {
                longToast(R.string.play_services_unavailable)
                finish()
            }
        }
    }
}
