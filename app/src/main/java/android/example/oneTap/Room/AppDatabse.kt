
package android.example.oneTap.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(myLocation::class), version = 3, exportSchema = false)
public abstract class AppDatabse : RoomDatabase() {
    abstract fun getLocationdao(): LocationDao


    companion object {
        @Volatile
        private var sInstance: AppDatabse? = null
        private const val DATABASE_NAME = "D1"

        fun getdatabase(context: Context): AppDatabse {
            return sInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabse::class.java,
                    "D1"
                ).fallbackToDestructiveMigration()
                    .build()
                sInstance = instance
                instance
            }
        }
    }
}