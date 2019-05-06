package km.com.doordash.common.di

import dagger.Module
import dagger.Provides
import km.com.doordash.common.api.RestaurantService
import retrofit2.Retrofit

@Module
class RestaurantModule {

    @Provides
    fun provideRestaurantService(retrofit: Retrofit): RestaurantService {
        return retrofit.create(RestaurantService::class.java)
    }
}