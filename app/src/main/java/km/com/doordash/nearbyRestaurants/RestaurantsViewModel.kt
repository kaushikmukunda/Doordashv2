package km.com.doordash.nearbyRestaurants

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import km.com.doordash.common.api.RestaurantRepository
import km.com.doordash.common.utils.LoadingState
import km.com.doordash.nearbyRestaurants.model.Restaurant
import km.com.doordash.nearbyRestaurants.paging.DataSourceFactory

class RestaurantsViewModel : ViewModel() {

    lateinit var restaurantLiveData: LiveData<PagedList<Restaurant>>
    lateinit var loadingStateLiveData: LiveData<LoadingState>
    lateinit var retryLiveData: LiveData<Completable>
    private val disposables = CompositeDisposable()

    fun init(restaurantRepository: RestaurantRepository) {
        val dataSourceFactory = DataSourceFactory(restaurantRepository, disposables)
        restaurantLiveData = dataSourceFactory.toLiveData(PAGELIST_CONFIG)
        loadingStateLiveData = Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.loadingState }
        retryLiveData = Transformations.switchMap(dataSourceFactory.sourceLiveData) { it.retry }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 10

        val PAGELIST_CONFIG = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
    }

}