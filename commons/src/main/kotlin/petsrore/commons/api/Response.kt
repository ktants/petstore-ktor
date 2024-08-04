package petsrore.commons.api

sealed class Response<out T> {

    data class Part<T, out L>(
        val data: T,
        val location: L,
        val hasMore: Boolean,
    ) : Response<T>()

    data class Single<T>(val data: T) : Response<T>()

    data class Error(
        val errorCode: String,
        val errorMessage: String,
    ) : Response<Nothing>()
}