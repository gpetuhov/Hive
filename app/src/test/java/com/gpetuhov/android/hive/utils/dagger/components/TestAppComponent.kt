package com.gpetuhov.android.hive.utils.dagger.components

import com.gpetuhov.android.hive.application.dagger.components.AppComponent
import com.gpetuhov.android.hive.interactors.DeleteUserInteractorTest
import com.gpetuhov.android.hive.interactors.SaveUsernameInteractorTest
import com.gpetuhov.android.hive.interactors.SignOutInteractorTest
import com.gpetuhov.android.hive.utils.dagger.modules.TestAppModule
import dagger.Component
import javax.inject.Singleton

// This Dagger's component is used in tests instead of AppComponent

@Component(modules = [TestAppModule::class])
@Singleton
interface TestAppComponent : AppComponent {
    fun inject(deleteUserInteractorTest: DeleteUserInteractorTest)
    fun inject(signOutInteractorTest: SignOutInteractorTest)
    fun inject(saveUsernameInteractorTest: SaveUsernameInteractorTest)
}
