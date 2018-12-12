package com.gpetuhov.android.hive.ui.epoxy.base

import android.os.Bundle
import com.airbnb.epoxy.EpoxyController

// Base controller for profile and user details
abstract class UserBaseController : EpoxyController() {

    companion object {
        private const val SELECTED_OFFER_PHOTO_MAP_KEY = "selectedOfferPhotoMap"
    }

    protected var selectedOfferPhotoMap = hashMapOf<String, Int>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_OFFER_PHOTO_MAP_KEY, selectedOfferPhotoMap)
    }

    override fun onRestoreInstanceState(inState: Bundle?) {
        super.onRestoreInstanceState(inState)
        val restored = inState?.getSerializable(SELECTED_OFFER_PHOTO_MAP_KEY)
        if (restored != null) selectedOfferPhotoMap = restored as HashMap<String, Int>
    }
}