package km.com.doordash.nearbyRestaurants

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Observable
import km.com.doordash.nearbyRestaurants.model.Restaurant
import km.com.doordash.nearbyRestaurants.paging.DataSourceFactory

class RestaurantsViewModel : ViewModel() {

    lateinit var restaurantObservable: Observable<PagedList<Restaurant>>

    fun init(dataSourceFactory: DataSourceFactory) {
        restaurantObservable =
            RxPagedListBuilder<Int, Restaurant>(dataSourceFactory, PAGELIST_CONFIG).buildObservable()
    }


    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5

        val PAGELIST_CONFIG = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
    }

}