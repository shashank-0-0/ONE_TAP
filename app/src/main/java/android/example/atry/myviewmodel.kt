package android.example.atry

import android.app.Application
import android.example.atry.Room.AppDatabse
import android.example.atry.Room.LocationDao
import android.example.atry.Room.myLocation
import android.example.myapplication.Repository.location_repository
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    var dao: LocationDao

    private var _location = MutableStateFlow<List<myLocation>>(listOf())
    val location =_location.asStateFlow()

    private val _save_task_event = MutableSharedFlow<Int>()
    val save_task_event=_save_task_event.asSharedFlow()


    init {
        dao = AppDatabse.getdatabase(app).getLocationdao()
        getsavedmovies(dao)
    }

    fun getsavedmovies(dao: LocationDao) {

        viewModelScope.launch {
             repository.all_locations(dao)?.collectLatest {
                 Log.i("#@#","got movies")
                _location.value=it
            }
        }
    }


    fun fetch_location(locationName: String) {
        Log.i("#@#","start of function")

        viewModelScope.launch {
            repository.fetch_location().collectLatest {
                Log.i("#@#","got the location")
                var address = getcity(it, geocoder)
                saveLocation(it, address, locationName)
                Log.i("#@#","yoo");
                _save_task_event.emit(1)
            }
            Log.i("#@#","end of coroutine scope")
        }
        Log.i("#@#","end of function")
    }



    private suspend fun saveLocation(location: Location?, address: String?, locationName: String?) {
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
    suspend fun deleteLocation(mylocation: myLocation?) {
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


