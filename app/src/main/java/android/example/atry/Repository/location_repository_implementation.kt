package android.example.myapplication.Repository

import android.annotation.SuppressLint
import android.example.atry.Room.LocationDao
import android.example.atry.Room.myLocation
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject



class location_repository_implementation
@Inject
constructor(
        var fusedLocationProviderClient: FusedLocationProviderClient
) : location_repository {



    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override fun fetch_location() = callbackFlow<Location> {
        Log.i("#@#","start of fetch location ")

        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        var _location : Location? = null

        val location_callback =object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {

                    Log.i("#@#","got location updates ${location.latitude} ")
                    _location=location
//                    Toast.makeText(t,"got location", Toast.LENGTH_SHORT).show()


                    offer(location)
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            location_callback,
            Looper.getMainLooper()
        )
        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(location_callback)
        }
        Log.i("#@#","end of fetch location")

    }

    override suspend fun delete(dao: LocationDao, location: myLocation) {
        dao.deleteLocation(location)
    }

    override suspend fun insert(dao:LocationDao, location: myLocation) {
        dao.insertnewLocation(location)
    }


    override suspend fun all_locations(dao:LocationDao): Flow<List<myLocation>> {
        return  dao.loadallLocation()
    }



}