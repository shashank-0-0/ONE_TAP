package android.example.Repository

import android.example.myapplication.Repository.location_repository
import android.example.oneTap.Room.LocationDao
import android.example.oneTap.Room.myLocation
import android.example.oneTap.Utils.Resource
import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//fake test double to simulate the behaviour of our repository so that we can test the viewmodel
class FakeLocationRepository : location_repository{

    private val locations = mutableListOf<myLocation>()
    private val observableLocations = flow<List<myLocation>>{
    }


    override suspend fun insert(location: myLocation) {
        locations.add(location)
    }

    override suspend fun delete(location: myLocation) {
        locations.remove(location)
    }

    override suspend fun all_locations(): Flow<List<myLocation>>? {
        return observableLocations
    }

    override fun fetch_location(): Flow<Resource<Location>> {
        TODO()
    }

}