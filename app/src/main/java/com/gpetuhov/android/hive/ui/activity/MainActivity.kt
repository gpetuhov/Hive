package com.gpetuhov.android.hive.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.repository.Repository
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

    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var repo: Repository

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HiveApp.appComponent.inject(this)

        initNavigation()
        initLocationManager()
        initAuthManager()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()
        checkPlayServicesAndPermissions()

        // TODO: remove this
        locationManager.startLocationUpdates()

        authManager.startListenAuth()

        updateUserOnlineStatus(true)
    }

    override fun onPause() {
        super.onPause()

        // TODO: remove this
        locationManager.stopLocationUpdates()
        repo.deleteLocation()

        authManager.stopListenAuth()

        updateUserOnlineStatus(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != Activity.RESULT_OK) {
                toast("Please, turn on geolocation")
                locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)
            }
        }
    }

    private fun initNavigation() {
        // Find NavController
        navController = findNavController(R.id.nav_host)

        // Tie NavHostFragment to bottom navigation bar
        navigation_view.setupWithNavController(navController)
    }

    private fun initLocationManager() {
        locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)

        // TODO: uncomment this
//        locationManager.startLocationUpdates()
    }

    private fun initAuthManager() {
        authManager.init(this::onSignIn, this::onSignOut)
    }

    private fun checkPlayServicesAndPermissions() {
        val playServicesAvailable = locationManager.checkPlayServices(this) {
            longToast(R.string.play_services_unavailable)
            finish()
        }

        if (playServicesAvailable) {
            if (!checkPermissions(this)) {
                startActivity<PermissionsActivity>()
                finish()
            }
        }
    }

    private fun onSignIn(user: User) {
        repo.updateUserNameAndEmail({ /* Do nothing */ }, { /* Do nothing */ })
    }

    private fun onSignOut() {
        // If signed out, just start AuthActivity and finish.
        // AuthActivity will handle the login process.
        startActivity<AuthActivity>()
        finish()
    }

    private fun updateUserOnlineStatus(isOnline: Boolean) {
        authManager.user.isOnline = isOnline
        repo.updateUserOnlineStatus({ /* Do nothing */ }, { /* Do nothing */ })
    }
}
