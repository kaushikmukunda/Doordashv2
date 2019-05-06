package km.com.doordash.nearbyRestaurants.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import km.com.doordash.common.api.RestaurantRepository
import km.com.doordash.common.utils.LoadingState
import km.com.doordash.nearbyRestaurants.model.Restaurant
import java.util.*

class DataSourceFactory constructor(
    private val restaurantRepository: RestaurantRepository,
    private val compositeDisposable: CompositeDisposable
) :
    DataSource.Factory<Int, Restaurant>() {

    val sourceLiveData = MutableLiveData<RestaurantDataSource>()

    override fun create(): RestaurantDataSource {
        val restaurantDataSource = RestaurantDataSource(restaurantRepository, compositeDisposable)
        sourceLiveData.postValue(restaurantDataSource)
        return restaurantDataSource
    }
}

class RestaurantDataSource(
    private val restaurantRepository: RestaurantRepository,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Restaurant>() {

    companion object {
        private val TAG: String = RestaurantDataSource::class.java.name
    }

    val loadingState = MutableLiveData<LoadingState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Restaurant>) {
        compositeDisposable.add(
            restaurantRepository.loadData(0)
                .doOnSubscribe { loadingState.postValue(LoadingState.LOADING) }
                .subscribe(
                    { restaurants ->
                        callback.onResult(restaurants, 0, restaurants.size)
                        loadingState.postValue(LoadingState.SUCCESS)
                    },
                    { throwable ->
                        Log.e(TAG, "error loading initial list", throwable)
                        callback.onResult(Collections.emptyList(), 0, 0)
                        loadingState.postValue(LoadingState.ERROR)
                    })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Restaurant>) {
        compositeDisposable.add(
            restaurantRepository.loadData(params.key)
                .doOnSubscribe { loadingState.postValue(LoadingState.LOADING) }
                .subscribe(
                    { restaurants ->
                        callback.onResult(restaurants, params.key + restaurants.size)
                        loadingState.postValue(LoadingState.SUCCESS)

                    },
                    { throwable ->
                        Log.e(TAG, "error loading initial list", throwable)
                        callback.onResult(Collections.emptyList(), params.key)
                        loadingState.postValue(LoadingState.ERROR)
                    })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Restaurant>) {
    }
}

