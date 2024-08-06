package petstore.commons.api

sealed class Response<out T> {

    data class Many<T, out L>(
        val items: T,
        val location: L,
        val hasMore: Boolean,
    ) : Response<T>()

    data class Single<T>(val item: T) : Response<T>()

    data class Error(
        val errorCode: String,
        val errorMessage: String,
    ) : Response<Nothing>()
}