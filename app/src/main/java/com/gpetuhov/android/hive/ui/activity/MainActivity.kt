package com.gpetuhov.android.hive.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.LocationManager
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
        private const val RC_SIGN_IN = 102
    }

    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var repo: Repository

    private lateinit var navController: NavController

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HiveApp.appComponent.inject(this)

        initNavigation()
        initLocationManager()

        firebaseAuth = FirebaseAuth.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                onSignedInInitialize(user.displayName)
            } else {
                // User is signed out
                onSignedOutCleanup()

                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build()
//                    AuthUI.IdpConfig.PhoneBuilder().build(),
//                    AuthUI.IdpConfig.GoogleBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build(),
//                    AuthUI.IdpConfig.TwitterBuilder().build()
                )

                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()
        checkPlayServicesAndPermissions()

        // TODO: remove this
        locationManager.startLocationUpdates()

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()

        // TODO: remove this
        locationManager.stopLocationUpdates()
        repo.deleteUser()

        firebaseAuth.removeAuthStateListener(authStateListener)
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

    private fun onSignedInInitialize(displayName: String?) {
        // TODO: implement this
    }

    private fun onSignedOutCleanup() {
        // TODO: implement this
    }
}
