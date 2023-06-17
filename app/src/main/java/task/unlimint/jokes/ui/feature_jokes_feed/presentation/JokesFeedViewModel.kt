package task.unlimint.jokes.ui.feature_jokes_feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import task.unlimint.jokes.core.util.UIState
import task.unlimint.jokes.customnetworkadapter.NetworkResponse
import task.unlimint.jokes.customnetworkadapter.executeWithRetry
import task.unlimint.jokes.ui.feature_jokes_feed.domain.use_case.JokesFeedScreenUseCases

class JokesFeedViewModel(
private val jokesFeedScreenUseCases: JokesFeedScreenUseCases
):ViewModel() {

    private val uiStateChannel = Channel<UIState>(Channel.BUFFERED)
    val uiStateFlow = uiStateChannel.receiveAsFlow()

    fun getARandomJoke(){

        viewModelScope.launch {

            uiStateChannel.send(UIState.Loading)

            val response = executeWithRetry (times = 10000) {
                jokesFeedScreenUseCases.getARandomJoke()
                    .also {
                        if (it is NetworkResponse.NetworkError) {
                            uiStateChannel.send(UIState.Error("NetworkError"))
                        }
                    }
            }
            when(response){

                is NetworkResponse.Success ->{
                    uiStateChannel.send(UIState.Success(response.body))
                }

                is NetworkResponse.NetworkError ->{
                    uiStateChannel.send(UIState.Error("NetworkError"))
                }

                is NetworkResponse.ServerError ->{
                    uiStateChannel.send(UIState.Error(response.body?.message?:""))
                }

                is NetworkResponse.UnknownError ->{
                    uiStateChannel.send(UIState.Error("UnknownError"))
                }

            }
        }
    }

}