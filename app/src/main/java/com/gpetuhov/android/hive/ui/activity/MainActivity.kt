package com.gpetuhov.android.hive.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.interactor.SaveOnlineInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.managers.NotificationManager
import com.gpetuhov.android.hive.ui.viewmodel.UnreadMessagesExistViewModel
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

    @Inject lateinit var repo: Repo
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var auth: Auth
    @Inject lateinit var mapManager: MapManager
    @Inject lateinit var notificationManager: NotificationManager

    private lateinit var navController: NavController

    private val saveOnlineInteractor = SaveOnlineInteractor(object : SaveOnlineInteractor.Callback {
        override fun onSaveOnlineComplete() {
            // Do nothing
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        HiveApp.appComponent.inject(this)

        initNavigation()
        initAuthManager()
        checkLocationSettings()

        val viewModel = ViewModelProviders.of(this).get(UnreadMessagesExistViewModel::class.java)
        viewModel.unreadMessagesExist.observe(this, Observer<Boolean> { unreadMessagesExist ->
            updateMessagesIcon(unreadMessagesExist)
        })
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onResume() {
        super.onResume()
        checkPlayServicesAndPermissions()
        repo.setForeground(true)
        auth.startListenAuth()
        notificationManager.onResume()
        updateUserOnlineStatus(true)
    }

    override fun onPause() {
        super.onPause()
        repo.setForeground(false)
        auth.stopListenAuth()
        notificationManager.onPause()
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

    override fun onDestroy() {
        super.onDestroy()

        // This is needed to reset map state if the app is closed by the user
        if (!isChangingConfigurations) mapManager.resetMapState()
    }

    // === Private methods ===

    private fun initNavigation() {
        // Find NavController
        navController = findNavController(R.id.nav_host)

        // Tie NavHostFragment to bottom navigation bar
        navigation_view.setupWithNavController(navController)
    }

    private fun checkLocationSettings() = locationManager.checkLocationSettings(this, REQUEST_CHECK_SETTINGS)

    private fun initAuthManager() = auth.init(this::onSignIn, this::onSignOut)

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

    private fun onSignIn() { /* Do nothing */ }

    private fun onSignOut() {
        // If signed out, just start AuthActivity and finish.
        // AuthActivity will handle the login process.
        startActivity<AuthActivity>()
        finish()
    }

    private fun updateUserOnlineStatus(isOnline: Boolean) = saveOnlineInteractor.saveOnline(isOnline)

    private fun updateMessagesIcon(unreadMessagesExist: Boolean) {
        val iconId = if (unreadMessagesExist) R.drawable.ic_mail_unread else R.drawable.ic_mail_read
        navigation_view.menu.findItem(R.id.navigation_messages).icon = ContextCompat.getDrawable(this, iconId)
    }
}
