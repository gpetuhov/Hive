package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.google.maps.android.ui.IconGenerator
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_LATITUDE
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_LONGITUDE
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_ZOOM
import com.gpetuhov.android.hive.util.Constants.Map.Companion.MIN_ZOOM
import timber.log.Timber
import javax.inject.Inject


// Show search results on map
class MapManager {

    interface Callback {
        fun onMinZoom()
        fun onMaxZoom()
        fun onNormalZoom()
        fun onCameraIdle(latitude: Double, longitude: Double, radius: Double)
        fun showDetails(uid: String)
    }

    companion object {
        private const val TAG = "MapManager"
        private const val LAT = "lat"
        private const val LON = "lon"
        private const val ZOOM = "zoom"
        private const val TILT = "tilt"
        private const val BEARING = "bearing"
        private const val MAPTYPE = "maptype"
        private const val QUERY_TEXT = "queryText"
        private const val USER_UID_KEY = "user_uid"
        private const val OFFER_UID_KEY = "offer_uid"
    }

    @Inject lateinit var context: Context
    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var repo: Repo

    private lateinit var callback: Callback
    private lateinit var googleMap: GoogleMap
    private var mapState: MapState? = null

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
        this.callback = callback

        // When the map is ready, get reference to it
        googleMap = map

        try {
            // Show my location (blue point)
            googleMap.isMyLocationEnabled = true

        } catch (e: SecurityException) {
            Timber.tag(TAG).d("Location permission not granted")
        }

        initCameraPosition()

        // Enable compass (will show on map rotate)
        googleMap.uiSettings.isCompassEnabled = true

        // Disable my location button
        googleMap.uiSettings.isMyLocationButtonEnabled = false

        // Disable zoom buttons
        googleMap.uiSettings.isZoomControlsEnabled = false

        // Set minimum and maximum zoom
        googleMap.setMinZoomPreference(Constants.Map.MIN_ZOOM)
        googleMap.setMaxZoomPreference(Constants.Map.MAX_ZOOM)

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
            val markerInfo = marker.tag as MutableMap<*, *>
            Timber.tag(TAG).d("Clicked on marker = $markerInfo")

            val userUid = markerInfo[USER_UID_KEY] as String?
            val offerUid = markerInfo[OFFER_UID_KEY] as String?

            if (userUid != null && !userUid.isEmpty()) {
                callback.showDetails(userUid)
            }

            true
        }
    }

    fun updateMarkers(searchResult: MutableMap<String, User>) {
        Timber.tag(TAG).d("Updating markers")

        googleMap.clear()

        searchResult.forEach { entry ->
            val user = entry.value
            val statusId = if (user.isOnline) R.string.online else R.string.offline
            val status = context.getString(statusId)
            val name = user.getUsernameOrName()
            val offerList = user.offerList
            val offerSearchResultIndex = user.offerSearchResultIndex

            val iconGenerator = IconGenerator(context)

            // TODO: restore these lines, when online status is properly detected
            // If user is online, set marker text color to green
//            if (user.isOnline) {
//                iconGenerator.setTextAppearance(R.style.greenTextStyle)
//            }
//
//            val iconBitmap = iconGenerator.makeIcon("${name} \n$status")

            val markerInfo = mutableMapOf<String, String>()
            markerInfo[USER_UID_KEY] = user.uid

            val markerText = if (offerSearchResultIndex >= 0 && offerSearchResultIndex < offerList.size) {
                // User contains offer that corresponds to search query text
                val offer = offerList[offerSearchResultIndex]
                markerInfo[OFFER_UID_KEY] = offer.uid

                val offerTitle = getOfferTitle(offer)
                val offerPrice = getOfferPrice(offer)
                "$offerTitle \n$offerPrice"

            } else {
                name
            }

            val iconBitmap = iconGenerator.makeIcon(markerText)

            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(user.location)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
            )

            marker.tag = markerInfo
        }
    }

    // Save map state into MapManager
    // (MapManager is alive during the whole app lifecycle)
    fun saveMapState(queryText: String) {
        if (::googleMap.isInitialized) {
            mapState = MapState(googleMap.cameraPosition, googleMap.mapType, queryText)
        }
    }

    // Save map state into savedInstanceState
    fun saveOutState(outState: Bundle) {
        // Save map state not only in MapManger itself,
        // but also into savedInstanceState bundle,
        // because Android keeps savedInstanceState bundle on orientation change
        // and if the app gets killed by the OS.
        outState.putDouble(LAT, mapState?.cameraPosition?.target?.latitude ?: Constants.Map.DEFAULT_LATITUDE)
        outState.putDouble(LON, mapState?.cameraPosition?.target?.longitude ?: Constants.Map.DEFAULT_LONGITUDE)
        outState.putFloat(ZOOM, mapState?.cameraPosition?.zoom ?: Constants.Map.MIN_ZOOM)
        outState.putFloat(TILT, mapState?.cameraPosition?.tilt ?: Constants.Map.DEFAULT_TILT)
        outState.putFloat(BEARING, mapState?.cameraPosition?.bearing ?: Constants.Map.DEFAULT_BEARING)
        outState.putInt(MAPTYPE, mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL)
        outState.putString(QUERY_TEXT, mapState?.queryText ?: "")
    }

    // Restore map state from savedInstanceState, if exists and contains saved map state.
    // Return saved query text or empty string.
    fun restoreMapState(savedInstanceState: Bundle?): String {
        if (savedInstanceState != null) {
            val latitude = savedInstanceState.getDouble(LAT, Constants.Map.DEFAULT_LATITUDE)
            val longitude = savedInstanceState.getDouble(LON, Constants.Map.DEFAULT_LONGITUDE)

            // If there are no latitude and longitude in the saved state,
            // then there is no saved map state in the bundle, so do not update mapState property.
            if (latitude != Constants.Map.DEFAULT_LATITUDE
                && longitude != Constants.Map.DEFAULT_LONGITUDE) {

                val target = LatLng(latitude, longitude)
                val zoom = savedInstanceState.getFloat(ZOOM, Constants.Map.MIN_ZOOM)
                val bearing = savedInstanceState.getFloat(BEARING, Constants.Map.DEFAULT_BEARING)
                val tilt = savedInstanceState.getFloat(TILT, Constants.Map.DEFAULT_TILT)
                val position = CameraPosition.Builder().target(target).zoom(zoom).tilt(tilt).bearing(bearing).build()

                val mapType = savedInstanceState.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL)

                val queryText = savedInstanceState.getString(QUERY_TEXT, "")

                mapState = MapState(position, mapType, queryText)
            }
        }

        return mapState?.queryText ?: ""
    }

    fun resetMapState() {
        mapState = null
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

    private fun initCameraPosition() {
        // If there is saved map state, move camera to saved camera position,
        // and set saved map type.
        if (mapState != null) {
            moveCamera(mapState?.cameraPosition)
            googleMap.mapType = mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL

        } else {
            // Otherwise move camera to current location
            locationManager.getLastLocation { location ->
                val zoom =
                    if (location.latitude == DEFAULT_LATITUDE && location.longitude == DEFAULT_LONGITUDE) MIN_ZOOM else DEFAULT_ZOOM

                val cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).build()
                moveCamera(cameraPosition)
            }
        }
    }

    private fun checkZoom() {
        val zoom = googleMap.cameraPosition.zoom

        when {
            zoom <= Constants.Map.MIN_ZOOM -> this.callback.onMinZoom()
            zoom >= Constants.Map.MAX_ZOOM -> this.callback.onMaxZoom()
            else -> this.callback.onNormalZoom()
        }
    }

    private fun moveCamera(cameraPosition: CameraPosition?) =
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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

    // === Inner classes ===

    data class MapState(
        var cameraPosition: CameraPosition,
        var mapType: Int,
        var queryText: String
    )
}