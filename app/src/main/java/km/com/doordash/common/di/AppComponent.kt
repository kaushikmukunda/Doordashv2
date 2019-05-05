package km.com.doordash.common.di

import dagger.Component
import km.com.doordash.nearbyRestaurants.RestaurantsActivity
import km.com.doordash.nearbyRestaurants.di.RestaurantModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RestaurantModule::class])
interface AppComponent {

    fun inject(activity: RestaurantsActivity)
}
