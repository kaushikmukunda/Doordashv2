package km.com.doordash.nearbyRestaurants.paging

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import km.com.doordash.nearbyRestaurants.api.RestaurantRepository
import km.com.doordash.nearbyRestaurants.model.Restaurant
import javax.inject.Inject

class DataSourceFactory @Inject constructor(private val restaurantRepository: RestaurantRepository) :
    DataSource.Factory<Int, Restaurant>() {
    override fun create(): RestaurantDataSource {
        return RestaurantDataSource(restaurantRepository)
    }
}

class RestaurantDataSource constructor(private val restaurantRepository: RestaurantRepository) :
    PageKeyedDataSource<Int, Restaurant>() {

    companion object {
        private val TAG: String = RestaurantDataSource::class.java.name
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Restaurant>) {
        restaurantRepository.loadData(0)
            .subscribe(
                { restaurants ->
                    callback.onResult(restaurants, 0, restaurants.size)

                },
                { throwable ->
                    Log.e(TAG, "error loading initial list", throwable)
                })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Restaurant>) {
        restaurantRepository.loadData(params.key)
            .subscribe(
                { restaurants ->
                    callback.onResult(restaurants, params.key + restaurants.size)

                },
                { throwable ->
                    Log.e(TAG, "error loading initial list", throwable)
                })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Restaurant>) {
    }
}

