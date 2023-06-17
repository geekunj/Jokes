package task.unlimint.jokes.core.util

sealed class UIState {
    object Loading: UIState()
    data class Error(val message: String): UIState()
    data class Success<out T>(val data: T): UIState()
    data class LoadingDueTo(val reason: String = ""): UIState()
}