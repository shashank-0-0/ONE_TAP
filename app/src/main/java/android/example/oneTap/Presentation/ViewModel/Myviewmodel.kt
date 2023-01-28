package android.example.oneTap.Presentation.ViewModel

import android.example.myapplication.Repository.location_repository
import android.example.oneTap.Room.myLocation
import android.example.oneTap.Utils.PermissionUtils
import android.example.oneTap.Utils.Resource
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class myviewmodel
@Inject
constructor(
    private val repository: location_repository,
    private val geocoder: Geocoder,
) : ViewModel() {


    private var _location = MutableStateFlow<List<myLocation>>(listOf())
    val location = _location.asStateFlow()

    private val _state = MutableSharedFlow<Resource<String>>()
    val state = _state.asSharedFlow()

    init {
        getSavedLocations()
    }

    //function to fetch all saved locations from database
    fun getSavedLocations() {
        viewModelScope.launch {
            repository.all_locations()?.collectLatest {
                _location.value = it
            }
        }
    }

//    fethcing the location and saving it in database
    suspend fun fetch_location(locationName: String = "") {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetch_location().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
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

        }
    }


//    function to save the fetched location in the database
    suspend fun saveLocation(location: Location?, address: String?, locationName: String?) {
        location?.let {
            repository.insert(
                myLocation(
                    lattitude = it.latitude,
                    longitude = it.longitude,
                    name = locationName,
                    address = address,
                )
            )
        }
    }

//    function to delete the saved location
    suspend fun deleteLocation(mylocation: myLocation?) {
        viewModelScope.launch {
            mylocation?.let {
                repository.delete(
                    mylocation
                )
            }
        }
    }

}
