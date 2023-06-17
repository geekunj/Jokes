package task.unlimint.jokes.ui.feature_jokes_feed.domain.use_case

import task.unlimint.jokes.customnetworkadapter.NetworkResponse
import task.unlimint.jokes.data.data_source.remote.ApiService
import task.unlimint.jokes.data.data_source.remote.dto.ApiError
import task.unlimint.jokes.data.data_source.remote.dto.GetJoke

class GetARandomJoke(private val apiService: ApiService) {
    suspend operator fun invoke(

    ): NetworkResponse<GetJoke, ApiError> {
        return apiService.getRandomJoke()
    }
}