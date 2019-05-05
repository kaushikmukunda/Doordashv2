package km.com.doordash.nearbyRestaurants.di

import dagger.Module
import dagger.Provides
import km.com.doordash.nearbyRestaurants.api.RestaurantService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RestaurantModule {

    @Provides
    fun provideRestuarantService(retrofit: Retrofit): RestaurantService {
        return retrofit.create(RestaurantService::class.java)
    }
}