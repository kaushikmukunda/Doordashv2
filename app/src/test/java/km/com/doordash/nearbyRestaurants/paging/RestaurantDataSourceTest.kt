package km.com.doordash.nearbyRestaurants.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import junit.framework.Assert.assertEquals
import km.com.doordash.common.api.RestaurantRepository
import km.com.doordash.common.utils.LoadingState
import km.com.doordash.nearbyRestaurants.model.Restaurant
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RestaurantDataSourceTest {


    @get:Rule
    val testSchedulerRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepository: RestaurantRepository

    @Mock
    lateinit var mockLifecycleOwner: LifecycleOwner

    @Mock
    lateinit var mockInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Restaurant>

    @Mock
    lateinit var mockCallback: PageKeyedDataSource.LoadCallback<Int, Restaurant>

    lateinit var restaurantDataSource: RestaurantDataSource
    lateinit var loadingStateObserver: Observer<LoadingState>

    @Before
    fun setUp() {
        restaurantDataSource = RestaurantDataSource(mockRepository, CompositeDisposable())
        loadingStateObserver = Observer { }

        val lifecycle = LifecycleRegistry(mockLifecycleOwner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @Test
    fun `when loadInitial succeeds new restaurants returned in callback`() {
        val mockRestaurants = getRestaurants(3)
        `when`(mockRepository.loadData(0)).thenReturn(Observable.just(mockRestaurants))
        restaurantDataSource.loadInitial(PageKeyedDataSource.LoadInitialParams(10, false), mockInitialCallback)

        verify(mockInitialCallback, times(1)).onResult(eq(mockRestaurants), eq(0), eq(mockRestaurants.size))
        assertEquals(LoadingState.SUCCESS, restaurantDataSource.loadingState.value)
    }

    @Test
    fun `when loadInitial fails callback not invoked`() {
        `when`(mockRepository.loadData(0)).thenReturn(Observable.error(UnsupportedOperationException()))
        restaurantDataSource.loadInitial(PageKeyedDataSource.LoadInitialParams(10, false), mockInitialCallback)

        verifyNoMoreInteractions(mockInitialCallback)
        assertEquals(LoadingState.ERROR, restaurantDataSource.loadingState.value)
    }

    @Test
    fun `when loadAfter succeeds new restaurants returned in callback`() {
        val mockRestaurants = getRestaurants(3)
        val offset = 10

        `when`(mockRepository.loadData(offset)).thenReturn(Observable.just(mockRestaurants))
        restaurantDataSource.loadAfter(PageKeyedDataSource.LoadParams(offset, 10), mockCallback)

        verify(mockCallback, times(1)).onResult(eq(mockRestaurants), eq(offset + mockRestaurants.size))
        assertEquals(LoadingState.SUCCESS, restaurantDataSource.loadingState.value)
    }

    @Test
    fun `when loadAfter fails callback not invoked`() {
        val offset = 10
        `when`(mockRepository.loadData(offset)).thenReturn(Observable.error(UnsupportedOperationException()))
        restaurantDataSource.loadAfter(PageKeyedDataSource.LoadParams(offset, 10), mockCallback)

        verifyNoMoreInteractions(mockCallback)
        assertEquals(LoadingState.ERROR, restaurantDataSource.loadingState.value)
    }

    private fun getRestaurants(size: Int = 3): MutableList<Restaurant> {
        val restaurants = mutableListOf<Restaurant>()
        for (i in 0 until size) {
            restaurants.add(Restaurant("$i", "res$i", "", "", ""))
        }

        return restaurants
    }
}