package km.com.doordash.nearbyRestaurants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import km.com.doordash.MyApplication
import km.com.doordash.R
import km.com.doordash.nearbyRestaurants.paging.DataSourceFactory
import km.com.doordash.nearbyRestaurants.paging.RestaurantAdapter
import javax.inject.Inject

class RestaurantsActivity : AppCompatActivity() {

    @Inject
    lateinit var dataSourceFactory: DataSourceFactory

    private lateinit var viewModel: RestaurantsViewModel
    private lateinit var adapter: RestaurantAdapter

    private lateinit var pageListSubscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        MyApplication.INSTANCE.appComponent.inject(this)

        initView()
    }

    override fun onResume() {
        super.onResume()
        pageListSubscription = viewModel.restaurantObservable.subscribe { restaurantPageList ->
            adapter.submitList(restaurantPageList)
        }
    }

    override fun onPause() {
        super.onPause()
        pageListSubscription.dispose()
    }

    private fun initView() {
        adapter = RestaurantAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel = ViewModelProviders.of(this).get(RestaurantsViewModel::class.java)
        viewModel.init(dataSourceFactory)
    }
}
