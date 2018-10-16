package com.gpetuhov.android.hive.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gpetuhov.android.hive.ui.activity.MainActivity
import com.pawegio.kandroid.startActivity

// Implementing splash screen by setting
// AppTheme.Launcher theme for all application in the manifest and
// changing theme to AppTheme in activity's onCreate() method
// by calling setTheme() before super.onCreate()
// has a known drawback when sometimes splash screen
// does not disappear if an activity is recreated by the OS.
// Solution is to implement splash screen as a separate activity
// with AppTheme.Launcher theme and setting AppTheme theme
// for all other activities in the manifest file.

// Background from the theme is displayed immediately on application start,
// before any activity's onCreate() method is called.
// When activity's onCreate() method is called, that means that application
// has already been started and it is time to hide splash screen.
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SplashActivity needs no layout.
        // Just start main activity and finish.

        startActivity<PermissionsActivity>()
        finish()
    }
}
