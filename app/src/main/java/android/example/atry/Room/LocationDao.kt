package android.example.atry.Room

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface LocationDao  {
    @Query("SELECT * FROM location ")
    fun loadallLocation(): LiveData<List<myLocation>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertnewLocation(location: myLocation?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLoation(location: myLocation?)

    @Delete
    suspend fun deleteLocation(location: myLocation?)

//    @Query("DELETE FROM Movie WHERE Title = :title")
//    fun deleteByTitle(title: String?): Int
//
//    @Query("SELECT flag FROM Movie Where Title=:title")
//    fun getflagfromdb(title: String?): Int
}