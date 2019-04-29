package com.gpetuhov.android.hive.domain.interactor.base

// Each interactor implements only one single use case.
// Interactors know nothing of the Android specific classes.
// For Android specific things interactors must call
// methods of the corresponding interfaces
// (which must be declared at domain layer).
// These interfaces must be implemented at outer layers.
// Each interactor has only one method, which is called
// by the corresponding presenter.
// Interactors return result to outer layers through callbacks.

interface Interactor {
    fun execute()
}