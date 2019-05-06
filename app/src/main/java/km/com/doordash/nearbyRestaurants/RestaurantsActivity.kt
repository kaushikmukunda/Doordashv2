package km.com.doordash.nearbyRestaurants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import km.com.doordash.MyApplication
import km.com.doordash.R
import km.com.doordash.common.api.RestaurantRepository
import km.com.doordash.common.utils.LoadingState
import km.com.doordash.nearbyRestaurants.model.Restaurant
import km.com.doordash.nearbyRestaurants.paging.DataSourceFactory
import km.com.doordash.nearbyRestaurants.paging.RestaurantAdapter
import javax.inject.Inject

class RestaurantsActivity : AppCompatActivity() {

    @Inject
    lateinit var restaurantRepository: RestaurantRepository

    private lateinit var viewModel: RestaurantsViewModel
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        MyApplication.INSTANCE.appComponent.inject(this)

        initView(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        viewModel.restaurantLiveData.observe(this, Observer<PagedList<Restaurant>> {
            adapter.submitList(it)
        })

        viewModel.loadingStateLiveData.observe(this, Observer<LoadingState> {
            adapter.setNetworkState(it)
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.restaurantLiveData.removeObservers(this)
        viewModel.loadingStateLiveData.removeObservers(this)
    }

    private fun initView(savedInstanceState: Bundle?) {
        adapter = RestaurantAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel = ViewModelProviders.of(this).get(RestaurantsViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.init(restaurantRepository)
        }
    }
}
