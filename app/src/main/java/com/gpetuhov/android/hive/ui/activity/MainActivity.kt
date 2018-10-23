package com.gpetuhov.android.hive.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
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
import com.gpetuhov.android.hive.service.LocationService
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
        initAuthManager()
        checkLocationSettings()

        // TODO: this should be done when user shares location only
        startLocationService()

        // TODO: stop location service when user stops sharing location
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()
        checkPlayServicesAndPermissions()
        authManager.startListenAuth()
        updateUserOnlineStatus(true)
    }

    override fun onPause() {
        super.onPause()
        authManager.stopListenAuth()
        updateUserOnlineStatus(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != Activity.RESULT_OK) {
                toast("Please, turn on geolocation")
                checkLocationSettings()
            }
        }
    }

    private fun initNavigation() {
        // Find NavController
        navController = findNavController(R.id.nav_host)

        // Tie NavHostFragment to bottom navigation bar
        navigation_view.setupWithNavController(navController)
    }

    private fun checkLocationSettings() {
        locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)
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
        // After successful sign in we must write to Firestore
        // user name and email received from Firebase Auth.
        repo.updateUserNameAndEmail(this::getUserData, this::getUserData)
    }

    private fun getUserData() {
        repo.getUserData(authManager.currentUser.value?.uid ?: "", { user -> onUserDataReceived(user) }, { /* Do nothing */ })
    }

    private fun onUserDataReceived(user: User) {
        val currentUser = authManager.currentUser.value
        currentUser?.username = user.username
        authManager.currentUser.value = currentUser

        // TODO: tell ProfileFragment to update
    }

    private fun onSignOut() {
        // If signed out, just start AuthActivity and finish.
        // AuthActivity will handle the login process.
        startActivity<AuthActivity>()
        finish()
    }

    private fun updateUserOnlineStatus(isOnline: Boolean) {
        val currentUser = authManager.currentUser.value
        currentUser?.isOnline = isOnline
        authManager.currentUser.value = currentUser
        repo.updateUserOnlineStatus({ /* Do nothing */ }, { /* Do nothing */ })
    }

    private fun startLocationService() {
        val intent = getLocationServiceIntent()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun getLocationServiceIntent(): Intent {
        return Intent(this, LocationService::class.java)
    }
}
