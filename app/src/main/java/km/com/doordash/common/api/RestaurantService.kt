package km.com.doordash.common.api

import io.reactivex.Observable
import km.com.doordash.nearbyRestaurants.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantService {

    @GET("restaurant/")
    fun getRestaurants(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<List<Restaurant>>
}