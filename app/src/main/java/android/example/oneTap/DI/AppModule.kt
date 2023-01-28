package android.example.oneTap.DI

import android.content.Context
import android.example.myapplication.Repository.location_repository
import android.example.myapplication.Repository.location_repository_implementation
import android.example.oneTap.BaseApplication
import android.example.oneTap.Room.AppDatabse
import android.example.oneTap.Room.LocationDao
import android.location.Geocoder
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

//dependency module to provide dependencies
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun getkey(): String {
        return "mykey";
    }

    @Singleton
    @Provides
    fun getint(): Int {
        return 100;
    }

    @Provides
    fun getFusedClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun getRepository(
        dao: LocationDao,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): location_repository {
        return location_repository_implementation(dao, fusedLocationProviderClient)
    }

    @Provides
    fun getGeoCoder(
        @ApplicationContext context: Context
    ): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }

    @Singleton
    @Provides
    fun getLocationDatabase(
        @ApplicationContext context: Context
    ): AppDatabse {
        return Room.databaseBuilder(
            context,
            AppDatabse::class.java,
            "D1"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun getLocationDao(
        appDatabase:AppDatabse
    ):LocationDao{
        return appDatabase.getLocationdao()
    }


}