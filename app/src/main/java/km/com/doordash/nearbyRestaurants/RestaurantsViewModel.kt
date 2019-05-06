package km.com.doordash.nearbyRestaurants

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.paging.toLiveData
import io.reactivex.Observable
import km.com.doordash.nearbyRestaurants.model.Restaurant
import km.com.doordash.nearbyRestaurants.paging.DataSourceFactory

class RestaurantsViewModel : ViewModel() {

    lateinit var restaurantLiveData: LiveData<PagedList<Restaurant>>

    fun init(dataSourceFactory: DataSourceFactory) {
        restaurantLiveData = dataSourceFactory.toLiveData(PAGELIST_CONFIG)
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