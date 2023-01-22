package android.example.myapplication.Repository

import android.example.oneTap.Room.LocationDao
import android.example.oneTap.Room.myLocation
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface location_repository {


     fun fetch_location() : Flow<Location>
     suspend fun insert(dao:LocationDao,location : myLocation)
     suspend fun delete(dao:LocationDao,location : myLocation)
     suspend fun all_locations(dao: LocationDao): Flow<List<myLocation>>?


}