package task.unlimint.jokes.data.data_source.remote

import retrofit2.http.GET
import retrofit2.http.Query
import task.unlimint.jokes.customnetworkadapter.NetworkResponse
import task.unlimint.jokes.data.data_source.remote.dto.ApiError
import task.unlimint.jokes.data.data_source.remote.dto.GetJoke

interface ApiService {

    @GET("api")
    suspend fun getRandomJoke(
        @Query("format") format: String = "json"
    ): NetworkResponse<GetJoke, ApiError>
}