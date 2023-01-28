
package android.example.oneTap.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

//entity class for ROOM
@Entity(tableName = "Location")
data class myLocation(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val lattitude: Double? = null,
    val longitude: Double? = null,
    val name :String?=null,
    val address : String?=null,
)