package android.example.myapplication.Repository

import android.example.atry.Room.LocationDao
import android.example.atry.Room.myLocation
import android.location.Location
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface location_repository {


     fun fetch_location() : Flow<Location>
     suspend fun insert(dao:LocationDao,location : myLocation)
     suspend fun delete(dao:LocationDao,location : myLocation)
     suspend fun all_locations(dao: LocationDao): Flow<List<myLocation>>?


}