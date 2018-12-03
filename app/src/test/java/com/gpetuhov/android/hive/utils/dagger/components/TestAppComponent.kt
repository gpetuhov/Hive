package com.gpetuhov.android.hive.utils.dagger.components

import com.gpetuhov.android.hive.application.dagger.components.AppComponent
import com.gpetuhov.android.hive.interactors.*
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
    fun inject(saveDescriptionInteractorTest: SaveDescriptionInteractorTest)
    fun inject(saveOnlineInteractorTest: SaveOnlineInteractorTest)
    fun inject(searchInteractorTest: SearchInteractorTest)
    fun inject(sendMessageInteractorTest: SendMessageInteractorTest)
    fun inject(deleteOfferInteractorTest: DeleteOfferInteractorTest)
}
