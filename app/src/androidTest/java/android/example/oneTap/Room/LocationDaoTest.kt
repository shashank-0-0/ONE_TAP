package android.example.oneTap.Room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest //Denotes its unit tests
class LocationDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    //making sure testcases runs one after another(synchronously) and stays inside the test scope
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    @Named("test_db")
    lateinit var database: AppDatabse

    private lateinit var dao: LocationDao

    //executes before every test cases ,,this way test cases is independent
    @Before
    fun setup() {
        //making hilt inject all our dependencies in this class
        hiltRule.inject()
        dao = database.getLocationdao()
    }

    //making sure the database instance is closed in the end
    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertNewLocation_test() = runBlockingTest {
        val location = myLocation(1, 4.55, 5.66, "name", "address")
        val location1 = myLocation(2, 6.55, 7.66, "name1", "address1")
        dao.insertnewLocation(location)
        dao.insertnewLocation(location1)
        val locations = dao.loadallLocation().take(1)

        assertThat(locations.toList().get(0)).contains(location1)
    }

    @Test
    fun deleteLocation_test() = runBlockingTest {
        val location = myLocation(1, 4.55, 5.66, "name", "address")
        val location1 = myLocation(2, 6.55, 7.66, "name1", "address1")
        dao.insertnewLocation(location)
        dao.insertnewLocation(location1)
        dao.deleteLocation(location)

        val locations=dao.loadallLocation().take(1)
        assertThat(locations.toList().get(0)).doesNotContain(location)
    }
}
