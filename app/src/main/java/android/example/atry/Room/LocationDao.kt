package android.example.atry.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationDao  {
    @Query("SELECT * FROM location ")
    fun loadallLocation(): Flow<List<myLocation>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertnewLocation(location: myLocation?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLoation(location: myLocation?)

    @Delete
    suspend fun deleteLocation(location: myLocation?)


}