package android.example.oneTap.Room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
//junit4 is a library that is used to test kotlin code that runs in jvm environment ,here we are inside android
//environment.so we tell junit4 that these tests are instrumented tests
@RunWith(AndroidJUnit4::class)
@SmallTest //Denotes its unit tests .@mediumTest for denoting its integrated test
class LocationDaoTest {


    //making sure testcases stays inside the test scope
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabse
    private lateinit var dao: LocationDao

    //executes before every test cases ,,this way test cases is independent
    @Before
    fun setup() {
        //making sure we allow access to roomdb with main thread ,cuz we cannot have multithreading in testcases cuz we
        //need independency in testcases
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabse::class.java,
        ).allowMainThreadQueries().build()

        dao = database.getLocationdao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertNewLocation() = runBlockingTest {
        val location = myLocation(1, 4.55, 5.66, "name", "address")
        val location1 = myLocation(2, 6.55, 7.66, "name1", "address1")
        dao.insertnewLocation(location)
        dao.insertnewLocation(location1)

        val locations = dao.loadallLocation().take(1)

        assertThat(locations.toList().get(0)).contains(location1)
    }

    @Test
    fun deleteLocation() = runBlockingTest {
        val location = myLocation(1, 4.55, 5.66, "name", "address")
        val location1 = myLocation(2, 6.55, 7.66, "name1", "address1")
        dao.insertnewLocation(location)
        dao.insertnewLocation(location1)
        dao.deleteLocation(location)

        val locations=dao.loadallLocation().take(1)
        assertThat(locations.toList().get(0)).doesNotContain(location)
    }
}
