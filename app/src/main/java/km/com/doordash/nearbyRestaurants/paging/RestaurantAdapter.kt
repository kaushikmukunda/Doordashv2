package km.com.doordash.nearbyRestaurants.paging

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import km.com.doordash.common.utils.LoadingState
import km.com.doordash.nearbyRestaurants.model.Restaurant

class RestaurantAdapter : PagedListAdapter<Restaurant, RecyclerView.ViewHolder>(RESTAURANT_DIFF_CALLBACK) {

    private var loadingState: LoadingState? = null
    private var retry: Completable = Completable.complete()

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == (itemCount - 1)) {
            VIEW_TYPE_PROGRESS
        } else {
            VIEW_TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_CONTENT -> return RestaurantRowVH.create(parent)
            VIEW_TYPE_PROGRESS -> return ProgressVH.create(parent)

            else -> throw IllegalArgumentException("Unknown view type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_CONTENT -> getItem(position)?.let { (holder as RestaurantRowVH).bind(it) }
            VIEW_TYPE_PROGRESS -> loadingState?.let { (holder as ProgressVH).bind(it, retry) }
        }
    }

    fun setNetworkState(newLoadingState: LoadingState) {
        val previousState = loadingState
        val hadExtraRow = hasExtraRow()

        loadingState = newLoadingState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newLoadingState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun setRetry(retry: Completable) {
        this.retry = retry
    }

    private fun hasExtraRow(): Boolean {
        return loadingState?.let { it != LoadingState.SUCCESS } ?: false
    }

    companion object {
        private val RESTAURANT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Restaurant>() {
            override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
                return oldItem == newItem
            }

        }

        private const val VIEW_TYPE_CONTENT = 0
        private const val VIEW_TYPE_PROGRESS = 1
    }
}

