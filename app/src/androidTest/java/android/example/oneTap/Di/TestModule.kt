package android.example.oneTap.Di

import android.content.Context
import android.example.oneTap.Room.AppDatabse
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

//Declaring dependencies for test cases
@Module
@InstallIn(SingletonComponent::class)
object TestModule {


    @Provides
    @Named("test_db")
    fun provideInMemoryDB(
        @ApplicationContext context: Context
    ) :AppDatabse{
        //making sure we allow access to roomdb with main thread ,cuz we cannot have multithreading in testcases cuz we
        //need independency in testcases
        return Room.inMemoryDatabaseBuilder(context,AppDatabse::class.java)
            .allowMainThreadQueries()
            .build()
    }

}