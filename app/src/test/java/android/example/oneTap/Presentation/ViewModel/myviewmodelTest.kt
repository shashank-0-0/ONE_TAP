package android.example.oneTap.Presentation.ViewModel

import android.example.myapplication.Repository.location_repository_implementation
import android.example.oneTap.Room.myLocation
import android.example.oneTap.Utils.PermissionUtils
import android.example.oneTap.Utils.Resource
import android.location.Geocoder
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class myviewmodelTest {


    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var repository: location_repository_implementation

    @Mock
    lateinit var geocoder: Geocoder

    lateinit var myviewmodel: myviewmodel


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        myviewmodel = myviewmodel(repository, geocoder)

    }

    @Test
    fun `get saved locations updates the live data`() {
        runBlocking {
            val mylocations = listOf(
                myLocation(
                    lattitude = 2.3,
                    longitude = 4.5,
                    name = "bangalore",
                    address = "address"
                )
            )

            //mocking the repository to return a mutablestateflow of locations
            Mockito.`when`(repository.all_locations())
                .thenReturn(MutableStateFlow<List<myLocation>>(mylocations))

            myviewmodel.getSavedLocations()
            val result = myviewmodel.location.value
            assertEquals(result, mylocations)
        }
    }

    @Test
    fun `fetch location updates loading state`() {
        runBlocking {
            //mocking the reposiotry fetch location function to return a loading state
            Mockito.`when`(repository.fetch_location())
                .thenReturn(MutableStateFlow(Resource.Loading<Location>()))
            myviewmodel.fetch_location("location1")
            val sharedFlowState = myviewmodel.state.first()
            assertThat(sharedFlowState).isInstanceOf(Resource.Loading::class.java)
        }
    }

    @Test
    fun `fetch location updates Error state`() {
        runBlocking {
            //mocking the reposiotry fetch location function to return a Error state
            Mockito.`when`(repository.fetch_location())
                .thenReturn(MutableStateFlow(Resource.Error<Location>("error message")))
            myviewmodel.fetch_location("location1")
            val sharedFlowState = myviewmodel.state.first()
            assertThat(sharedFlowState).isInstanceOf(Resource.Error::class.java)
        }
    }

    @Test
    fun `fetch location updates Success state`() {
        runBlocking {
            val mock = Resource.Success<Location>(Location("provider"))
            //mocking the reposiotry fetch location function to return a Error state
            Mockito.`when`(repository.fetch_location())
                .thenReturn(MutableStateFlow(mock))

            //using mockk library to mock singleton objects
            mockkObject(PermissionUtils)
            every {
                PermissionUtils.getcity(any(), any())
            } returns ("city")

            myviewmodel.fetch_location("name")
            val sharedFlowState = myviewmodel.state.first()
            assertThat(sharedFlowState).isInstanceOf(Resource.Success::class.java)
        }
    }


}
