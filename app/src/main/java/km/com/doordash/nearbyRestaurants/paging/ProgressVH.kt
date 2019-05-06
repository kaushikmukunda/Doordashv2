package km.com.doordash.nearbyRestaurants.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import km.com.doordash.R
import km.com.doordash.common.utils.LoadingState

class ProgressVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorText: TextView = itemView.findViewById(R.id.error_msg)
    private val retryBtn: Button = itemView.findViewById(R.id.retry_btn)

    fun bind(loadingState: LoadingState, retry: Completable) {

        progressBar.visibility = getVisibility(loadingState == LoadingState.LOADING)
        errorText.visibility = getVisibility(loadingState == LoadingState.ERROR)
        retryBtn.visibility = getVisibility(loadingState == LoadingState.ERROR)

        retryBtn.setOnClickListener { retry.subscribe() }
    }

    private fun getVisibility(value: Boolean): Int {
        return if (value) View.VISIBLE else View.GONE
    }

    companion object {

        fun create(parent: ViewGroup): ProgressVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress_view, parent, false)
            return ProgressVH(view)
        }
    }

}