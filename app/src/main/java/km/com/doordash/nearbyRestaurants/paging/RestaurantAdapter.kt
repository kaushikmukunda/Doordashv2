package km.com.doordash.nearbyRestaurants.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import km.com.doordash.R
import km.com.doordash.nearbyRestaurants.model.Restaurant

private val restaurantDiffCallback = object : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

}

class RestaurantAdapter : PagedListAdapter<Restaurant, RestaurantRowVH>(restaurantDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantRowVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_row, parent, false)
        return RestaurantRowVH(view)
    }

    override fun onBindViewHolder(holder: RestaurantRowVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class RestaurantRowVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView.findViewById(R.id.thumbnail)

    fun bind(restaurant: Restaurant) {
        Glide.with(imageView)
            .load(restaurant.coverImgUrl)
            .into(imageView)
    }

}