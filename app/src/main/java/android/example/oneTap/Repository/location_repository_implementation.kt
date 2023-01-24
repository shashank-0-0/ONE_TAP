package android.example.myapplication.Repository

import android.annotation.SuppressLint
import android.example.oneTap.Room.LocationDao
import android.example.oneTap.Room.myLocation
import android.example.oneTap.Utils.Resource
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class location_repository_implementation
@Inject
constructor(
    var dao: LocationDao,
    var fusedLocationProviderClient: FusedLocationProviderClient
) : location_repository {


    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")

    override fun fetch_location() = callbackFlow<Resource<Location>> {
        Log.i("#@#","start of fetch location ")
        offer(Resource.Loading())

        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val location_callback =object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {

                    Log.i("#@#","got location updates ${location.latitude} ")
                    offer(Resource.Success(location))
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            location_callback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            offer(Resource.Error(it.localizedMessage ?: "error"))
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(location_callback)
        }
        Log.i("#@#","end of fetch location")
    }

    override suspend fun delete(location: myLocation) {
        dao.deleteLocation(location)
    }

    override suspend fun insert(location: myLocation) {
        dao.insertnewLocation(location)
    }

    override suspend fun all_locations(): Flow<List<myLocation>> {
        return dao.loadallLocation()
    }

}