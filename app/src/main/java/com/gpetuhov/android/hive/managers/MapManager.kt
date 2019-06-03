package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.base.BaseMapManager
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.getStringKeyMapFromGeneric
import timber.log.Timber
import javax.inject.Inject


// Show search results on map
class MapManager : BaseMapManager() {

    interface Callback {
        fun onMinZoom()
        fun onMaxZoom()
        fun onNormalZoom()
        fun onCameraIdle(latitude: Double, longitude: Double, radius: Double)
        fun showDetails(userUid: String, offerUid: String)
    }

    companion object {
        private const val TAG = "MapManager"
        private const val USER_UID_KEY = "user_uid"
        private const val OFFER_UID_KEY = "offer_uid"
    }

    @Inject lateinit var context: Context
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var repo: Repo

    private lateinit var callback: Callback

    private var markersMap = mutableMapOf<String, Marker>()

    init {
        HiveApp.appComponent.inject(this)
    }

    // Calling this method forces Google Maps (Google Play Services) to load.
    // So afterwards, when the map is really needed, it will load faster.
    fun initGoogleMaps(activity: Activity, savedInstanceState: Bundle?) {
        val dummyMapView = MapView(activity)
        dummyMapView.onCreate(savedInstanceState)
        dummyMapView.onDestroy()
    }

    fun initMap(callback: Callback, map: GoogleMap) {
        super.initMap(locationManager, map, false, true)

        this.callback = callback

        val topPaddingInPixels = context.resources.getDimensionPixelOffset(R.dimen.map_top_padding)
        googleMap.setPadding(0, topPaddingInPixels, 0, 0)

        // This is called when camera becomes idle after moving
        googleMap.setOnCameraIdleListener {
            checkZoom()

            val visibleRegion = googleMap.projection.visibleRegion
            val farLeft = visibleRegion.farLeft

            val target = googleMap.cameraPosition.target
            val radius = SphericalUtil.computeDistanceBetween(target, farLeft) / 1000

            callback.onCameraIdle(target.latitude, target.longitude, radius)
        }

        googleMap.setOnMarkerClickListener { marker ->
            val markerTag = marker.tag as MutableMap<*, *>
            val markerInfo = getStringKeyMapFromGeneric(markerTag)
            Timber.tag(TAG).d("Clicked on marker = $markerInfo")

            val userUid = markerInfo[USER_UID_KEY] as String?
            val offerUid = markerInfo[OFFER_UID_KEY] as String?

            if (userUid != null && userUid.isNotEmpty()) {
                callback.showDetails(userUid, offerUid ?: "")
            }

            true
        }
    }

    fun updateMarkers(searchResult: MutableMap<String, User>) {
        Timber.tag(TAG).d("Updating markers")

        // Remove markers for users, that are no longer in search result
        val markerUserUidsToRemove = mutableListOf<String>()
        markerUserUidsToRemove.addAll(markersMap.keys)
        markerUserUidsToRemove.removeAll(searchResult.keys)
        markerUserUidsToRemove.forEach { userUid ->
            Timber.tag(TAG).d("Removing marker for user uid $userUid")
            // Remove marker from Google Map
            markersMap[userUid]?.remove()
            // Remove marker from markers HashMap
            markersMap.remove(userUid)
        }

        // Add markers for users, that are new in search result
        val searchResultUserUidsToAdd = mutableListOf<String>()
        searchResultUserUidsToAdd.addAll(searchResult.keys)
        searchResultUserUidsToAdd.removeAll(markersMap.keys)
        searchResultUserUidsToAdd.forEach { userUid ->
            val user = searchResult[userUid]
            if (user != null) {
                Timber.tag(TAG).d("Adding marker for user uid $userUid")

                val name = user.getUsernameOrName()
                val offerList = user.offerList
                val offerSearchResultIndex = user.offerSearchResultIndex

                val iconGenerator = IconGenerator(context)

                // Contains user and offer uid of the corresponding marker
                // (this is needed to open user or offer details on marker click)
                val markerInfo = mutableMapOf<String, String>()
                markerInfo[USER_UID_KEY] = user.uid

                val markerText = if (offerSearchResultIndex >= 0 && offerSearchResultIndex < offerList.size) {
                    // User contains offer that corresponds to search query text
                    // Show this offer on map and include in marker info
                    val offer = offerList[offerSearchResultIndex]
                    markerInfo[OFFER_UID_KEY] = offer.uid

                    val offerTitle = getOfferTitle(offer)
                    val offerPrice = getOfferPrice(offer)
                    "$offerTitle \n$offerPrice"

                } else {
                    // User does not contain offer that corresponds to search query,
                    // just show user name.
                    name
                }

                val iconBitmap = iconGenerator.makeIcon(markerText)

                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(user.location)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                )

                marker.tag = markerInfo

                markersMap[user.uid] = marker
            }
        }
    }

    fun moveToCurrentLocation() {
        val location = repo.currentUser().value?.location

        if (location != null
            && location.latitude != Constants.Map.DEFAULT_LATITUDE
            && location.longitude != Constants.Map.DEFAULT_LONGITUDE) {

            val cameraPosition = CameraPosition.Builder(googleMap.cameraPosition).target(location).build()
            animateCamera(cameraPosition)
        }
    }

    fun zoomIn() {
        val zoom = googleMap.cameraPosition.zoom
        if (zoom < Constants.Map.MAX_ZOOM) zoom(zoom + 1)
    }

    fun zoomOut() {
        val zoom = googleMap.cameraPosition.zoom
        if (zoom > Constants.Map.MIN_ZOOM) zoom(zoom - 1)
    }

    // === Private methods ===

    private fun checkZoom() {
        val zoom = googleMap.cameraPosition.zoom

        when {
            zoom <= Constants.Map.MIN_ZOOM -> this.callback.onMinZoom()
            zoom >= Constants.Map.MAX_ZOOM -> this.callback.onMaxZoom()
            else -> this.callback.onNormalZoom()
        }
    }

    private fun animateCamera(cameraPosition: CameraPosition?) =
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    private fun zoom(zoom: Float) =
        animateCamera(CameraPosition.Builder(googleMap.cameraPosition).zoom(zoom).build())

    private fun getOfferTitle(offer: Offer): String {
        return if (offer.title.length <= Constants.Map.MAX_OFFER_TITLE_LENGTH) offer.title
        else "${offer.title.substring(0, Constants.Map.MAX_OFFER_TITLE_LENGTH)}..."
    }

    private fun getOfferPrice(offer: Offer): String =
        if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD"
}