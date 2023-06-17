package task.unlimint.jokes.di


import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import task.unlimint.jokes.BuildConfig
import task.unlimint.jokes.customnetworkadapter.NetworkResponseAdapterFactory
import task.unlimint.jokes.data.data_source.remote.ApiClient
import task.unlimint.jokes.data.data_source.remote.ApiService
import task.unlimint.jokes.ui.feature_jokes_feed.domain.use_case.GetARandomJoke
import task.unlimint.jokes.ui.feature_jokes_feed.domain.use_case.JokesFeedScreenUseCases
import task.unlimint.jokes.ui.feature_jokes_feed.presentation.JokesFeedViewModel

val appModule = module {

    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(ApiClient.makeOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    fun provideJokesFeedScreenUseCases(
        apiService: ApiService
    ): JokesFeedScreenUseCases {
        return JokesFeedScreenUseCases(
            getARandomJoke = GetARandomJoke(apiService),
        )
    }

    single<ApiService> { provideApiService() }

    single<JokesFeedScreenUseCases> { provideJokesFeedScreenUseCases(get()) }

    viewModel {
        JokesFeedViewModel(get())
    }

}