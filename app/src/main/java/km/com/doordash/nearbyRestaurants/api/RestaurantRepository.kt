package km.com.doordash.nearbyRestaurants.api

import io.reactivex.Observable
import km.com.doordash.nearbyRestaurants.model.Restaurant
import javax.inject.Inject

class RestaurantRepository @Inject constructor(private val restaurantService: RestaurantService) {

    companion object {
        const val limit: Int = 20
        const val latitude: String = "37.422740"
        const val longitude: String = "-122.139956"
    }

    fun loadData(offset: Int): Observable<List<Restaurant>> {
        return restaurantService.getRestaurants(latitude, longitude, offset, limit)
    }
}