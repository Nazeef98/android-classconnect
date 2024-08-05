package com.example.class_connect1.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

object LocationUtils {

    fun getLocationFromAddress(context: Context, address: String, city: String): LatLng? {
        val coder = Geocoder(context, Locale.getDefault())
        val addressList: List<Address>?
        var latLng: LatLng? = null

        try {
            // Combine address and city for geocoding
            val locationName = "$address, $city"
            addressList = coder.getFromLocationName(locationName, 5)
            if (addressList == null || addressList.isEmpty()) {
                return null
            }
            val location: Address = addressList[0]
            latLng = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            Log.e("LocationUtils", "Geocoding failed", ex)
        }

        return latLng
    }
}
