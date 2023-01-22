package android.example.oneTap.Presentation.ViewModel

import android.app.Application
import android.example.oneTap.Utils.PermissionUtils
import android.example.oneTap.Room.LocationDao
import android.example.oneTap.Room.myLocation
import android.example.myapplication.Repository.location_repository
import android.example.oneTap.Utils.Resource
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val dao: LocationDao
) : AndroidViewModel(app) {


    private var _location = MutableStateFlow<List<myLocation>>(listOf())
    val location =_location.asStateFlow()

    private val _state = MutableSharedFlow<Resource<String>>()
    val state=_state.asSharedFlow()


    init {
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


    suspend fun fetch_location(locationName: String="") {
        Log.i("#@#","start of function")

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetch_location().collectLatest { result ->
                when(result){
                    is Resource.Success -> {
                        Log.i("#@#","got the location")
                        var address = PermissionUtils.getcity(result.data, geocoder)
                        saveLocation(result.data, address, locationName)
                        _state.emit(Resource.Success("success"))
                    }
                    is Resource.Error -> {
                        _state.emit(Resource.Error(result.message ?: "some error occured"))
                    }
                    is Resource.Loading -> {
                        _state.emit(Resource.Loading())
                    }
                }
            }
            Log.i("#@#","end of coroutine scope")
        }
        Log.i("#@#","end of function")
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

}


