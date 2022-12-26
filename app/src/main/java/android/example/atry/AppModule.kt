package android.example.atry

import android.app.Application
import android.content.Context
import android.example.myapplication.Repository.location_repository
import android.example.myapplication.Repository.location_repository_implementation
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

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
    fun getkey():String{
        return "mykey";
    }
    @Singleton
    @Provides
    fun getint():Int{
        return 100;
    }

    @Provides
    fun getFusedClient(@ApplicationContext context:Context):FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun getRepository(
        fusedLocationProviderClient: FusedLocationProviderClient
    ):location_repository{
        return location_repository_implementation(fusedLocationProviderClient)
    }

    @Provides
    fun getGeoCoder(
        @ApplicationContext context:Context
    ):Geocoder{
        return Geocoder(context, Locale.getDefault())
    }




}