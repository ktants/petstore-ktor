package petstore.commons

fun <T> Iterator<T>.fetch(): T? = if (hasNext()) next() else null