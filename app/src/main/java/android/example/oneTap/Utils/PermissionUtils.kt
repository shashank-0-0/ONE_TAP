package android.example.oneTap.Utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.example.oneTap.R
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    /**
     * Function to request permission from the user
     */
    fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestId
        )
    }

    /**
     * Function to check if the location permissions are granted or not
     */
    fun isAccessFineLocationGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Function to check if location of the device is enabled or not
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Function to show the "enable GPS" Dialog box
     */
    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.enable_gps))
            .setMessage(context.getString(R.string.required_for_this_app))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }
    fun getcity(location: Location?, geocoder: Geocoder): String {
        var address = ""
        location?.let {

            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city = addresses?.get(0)?.locality
            val state = addresses?.get(0)?.adminArea
            val subarea = addresses?.get(0)?.subLocality
            Log.i("QWE", addresses?.get(0)?.subLocality.toString())
            address = subarea + "," + city
        }
        Log.i("QWE", "$address")

        return address
    }


}
