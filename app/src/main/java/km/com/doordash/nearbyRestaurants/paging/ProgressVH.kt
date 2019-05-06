package km.com.doordash.nearbyRestaurants.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import km.com.doordash.R
import km.com.doordash.common.utils.LoadingState

class ProgressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorText: TextView = itemView.findViewById(R.id.error_msg)

    fun bind(loadingState: LoadingState) {
        when (loadingState) {

            LoadingState.LOADING -> {
                progressBar.visibility = View.VISIBLE
                errorText.visibility = View.GONE
            }
            LoadingState.SUCCESS -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.GONE
            }
            LoadingState.ERROR -> {
                progressBar.visibility = View.GONE
                errorText.visibility = View.VISIBLE
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup): ProgressVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress_view, parent, false)
            return ProgressVH(view)
        }
    }

}