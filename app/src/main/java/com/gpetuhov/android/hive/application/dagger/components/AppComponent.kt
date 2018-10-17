package com.gpetuhov.android.hive.application.dagger.components

import com.gpetuhov.android.hive.application.dagger.modules.AppModule
import com.gpetuhov.android.hive.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
