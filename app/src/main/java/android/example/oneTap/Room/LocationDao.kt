package android.example.oneTap.Room

import androidx.room.*
import kotlinx.coroutines.flow.Flow


//Defining the dao
@Dao
interface LocationDao  {

    @Query("SELECT * FROM location ")
    fun loadallLocation(): Flow<List<myLocation>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertnewLocation(location: myLocation?)

    @Delete
    suspend fun deleteLocation(location: myLocation?)


}