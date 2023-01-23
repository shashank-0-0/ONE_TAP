package android.example.myapplication.Repository

import android.example.oneTap.Room.LocationDao
import android.example.oneTap.Room.myLocation
import android.example.oneTap.Utils.Resource
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface location_repository {


     fun fetch_location() : Flow<Resource<Location>>
     suspend fun insert(location : myLocation)
     suspend fun delete(location : myLocation)
     suspend fun all_locations(): Flow<List<myLocation>>?


}