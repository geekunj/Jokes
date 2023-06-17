package task.unlimint.jokes.data.data_source.remote.dto

import com.google.gson.annotations.SerializedName

data class GetJoke(
    @SerializedName("joke")
    val joke: String,
)
