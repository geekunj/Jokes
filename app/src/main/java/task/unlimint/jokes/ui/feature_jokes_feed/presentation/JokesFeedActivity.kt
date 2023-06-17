package task.unlimint.jokes.ui.feature_jokes_feed.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import task.unlimint.jokes.R
import task.unlimint.jokes.core.util.Prefs
import task.unlimint.jokes.core.util.UIState
import task.unlimint.jokes.core.util.observeInLifecycle
import task.unlimint.jokes.data.data_source.remote.dto.GetJoke
import task.unlimint.jokes.databinding.ActivityJokesFeedBinding
import java.util.Timer
import java.util.TimerTask


class JokesFeedActivity : AppCompatActivity(), JokesListAdapter.Interaction {

    private val jokesFeedViewModel by viewModel<JokesFeedViewModel>()

    private lateinit var binding: ActivityJokesFeedBinding
    private lateinit var jokesListAdapter: JokesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_jokes_feed)
        binding.lifecycleOwner = this

        setupAdapters()

        jokesFeedViewModel.getARandomJoke()

        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                jokesFeedViewModel.getARandomJoke()
            }
        }
        timer.schedule(task, 1000 * 60, 1000 * 60)

        jokesFeedViewModel.uiStateFlow.onEach { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.pbProgress.visibility = View.VISIBLE
                }
                is UIState.Success<*> -> {

                    when(uiState.data) {
                        is GetJoke -> {
                            updateUi(uiState.data)
                        }
                    }
                }
                is UIState.Error -> {
                    binding.pbProgress.visibility = View.GONE
                }
                else -> {
                    binding.pbProgress.visibility = View.GONE
                }
            }
        }.observeInLifecycle(this)


    }

    private fun updateUi(data: GetJoke) {
        val jokesList = Prefs.getNonPrimitiveData<ArrayList<String>?>(object :
            TypeToken<ArrayList<String>?>() {}.type, "JokesList")?: arrayListOf()
        if (jokesList.isEmpty()) {
            jokesList.add(data.joke)
            Prefs.putNonPrimitiveData("JokesList", jokesList)
        } else {
            if (!jokesList.contains(data.joke)) {
                jokesList.add(data.joke)
                Prefs.putNonPrimitiveData("JokesList", jokesList)
            }
        }
        jokesListAdapter.addData(jokesList.asReversed())
        binding.pbProgress.visibility = View.GONE
    }

    private fun setupAdapters() {
        jokesListAdapter = JokesListAdapter(interaction = this)
        binding.rvJokes.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvJokes.adapter = jokesListAdapter

        val jokesList = Prefs.getNonPrimitiveData<ArrayList<String>>(object :
            TypeToken<ArrayList<String>>() {}.type, "JokesList")
        if (!jokesList.isNullOrEmpty()) {
            jokesListAdapter.addData(jokesList.asReversed())
        }
    }

    override fun onJokeItemClicked(position: Int, item: String) {

    }
}