package task.unlimint.jokes.ui.feature_jokes_feed.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import task.unlimint.jokes.R
import task.unlimint.jokes.databinding.ListItemJokeBinding

class JokesListAdapter(
    private val list: ArrayList<String> = arrayListOf(),
    private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemJokeBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.list_item_joke,
            parent,
            false
        )
        return BrandViewHolder(binding, interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as BrandViewHolder).bind(list[position])
    }

    fun addData(jokeList: List<String>) {

        list.clear()
        list.addAll(jokeList)
        notifyDataSetChanged()
    }

    inner class BrandViewHolder(
        val binding: ListItemJokeBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {

            binding.tvJoke.text = item

            binding.root.setOnClickListener{
                interaction?.onJokeItemClicked(adapterPosition, item)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size.coerceAtMost(10)
    }

    interface Interaction {
        fun onJokeItemClicked(position: Int, item: String)
    }

}