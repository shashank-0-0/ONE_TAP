
package android.example.atry.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Location")
data class myLocation(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val lattitude: Double? = null,
    val longitude: Double? = null,
    val name :String?=null,
    val address : String?=null,
)