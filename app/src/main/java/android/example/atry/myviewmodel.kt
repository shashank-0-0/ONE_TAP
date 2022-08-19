package android.example.atry

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.example.atry.Room.AppDatabse
import android.example.atry.Room.LocationDao
import android.example.atry.Room.myLocation
import android.example.myapplication.Repository.location_repository
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class myviewmodel
@Inject
constructor(
    app: Application,
    private val repository: location_repository,
    private val client: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) : AndroidViewModel(app) {

    var _location: LiveData<List<android.example.atry.Room.myLocation>>? = MutableLiveData()
    lateinit var dao: LocationDao
    private var fetched_location: Location? = null


    init {
        dao = AppDatabse.getdatabase(app).getLocationdao()
        _location = getsavedmovies(dao)
    }

    fun getsavedmovies(dao: LocationDao): LiveData<List<android.example.atry.Room.myLocation>>? {
        var locations: LiveData<List<myLocation>>? = null
        viewModelScope.launch {
            locations = repository.all_locations(dao)
        }
        return locations
    }

    fun fetch_location() {
        viewModelScope.launch {
            repository.fetch_location {
                Log.i("QWE", "${it?.latitude}")
                fetched_location = it
            }
        }
    }

//    fun save_location(locationName: String,callback :()-> Unit){
//        viewModelScope.launch {
//            Log.i("#@#","inside of coroutine")
//
//            repository.fetch_location{
//                Log.i("#@#","received call back in viewmodel")
//                fetched_location=it
//                viewModelScope.launch {
//
//                    var address = getcity(fetched_location, geocoder)
//                    saveLocation(fetched_location, address, locationName)
//                    Log.i("#@#", "saved location in viewmodel")
//
//                    callback()
//                }
//            }
//            Log.i("#@#","end of coroutine")
//        }
//        Log.i("#@#", "end of savelocation")
//    }

    @SuppressLint("MissingPermission")
    fun save_location(locationName: String, callback: (Int) -> Unit) {

        var task = client.lastLocation
        task.addOnSuccessListener {
            Log.i("#@#", "started on success listener")

            viewModelScope.launch {
                Log.i("#@#", "start of coroutine")
                var address = getcity(it, geocoder)
                saveLocation(it, address, locationName)
                Log.i("#@#", "saved location in viewmodel")
                callback(1)
            }
            Log.i("#@#", "end of on success listener")
        }
        task.addOnFailureListener {
            callback(0)
        }




        Log.i("#@#", "end of savelocation")
    }

    suspend fun saveLocation(location: Location?, address: String?, locationName: String?) {
        Log.i("!@!", "start of savelocation")

        location?.let {
            Log.i("QWE", "start of insert $address $locationName")
            repository.insert(
                dao,
                myLocation(
                    lattitude = it.latitude,
                    longitude = it.longitude,
                    name = locationName,
                    address = address,
                )
            )
        }
    }
    suspend fun delete_location(mylocation: myLocation?) {
        viewModelScope.launch {
            mylocation?.let {
                repository.delete(
                    dao,
                    mylocation
                )
            }
        }
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



    override fun onCleared() {
        super.onCleared()
        Log.i("#@#", "cleared viewmodel")
    }
}


