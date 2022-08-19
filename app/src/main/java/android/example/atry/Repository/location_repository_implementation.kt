package android.example.myapplication.Repository

import android.annotation.SuppressLint
import android.example.atry.Room.LocationDao
import android.example.atry.Room.myLocation
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import javax.inject.Inject



class location_repository_implementation
@Inject
constructor(
        var fusedLocationProviderClient: FusedLocationProviderClient
) : location_repository {




    @SuppressLint("MissingPermission")
    override fun fetch_location(callback: (Location?) -> Unit) {
        Log.i("#@#","start of fetch location ")

        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        var _location : Location? = null
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {

                        Log.i("#@#","got location updates ${location.latitude} ")
                        _location=location
                        callback(location)
                    }
                    fusedLocationProviderClient.removeLocationUpdates(this)

                }
            },
            Looper.myLooper()!!
        )
        Log.i("#@#","end of fetch location")

    }

    override suspend fun delete(dao: LocationDao, location: myLocation) {
        dao.deleteLocation(location)
    }

    override suspend fun insert(dao:LocationDao, location: myLocation) {
        dao.insertnewLocation(location)
    }


    override suspend fun all_locations(dao:LocationDao): LiveData<List<android.example.atry.Room.myLocation>> {
        return  dao.loadallLocation()
    }



}