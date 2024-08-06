@file:JvmName("Kotlin")

package petstore.commons

fun <T> forEach(vararg ts: T, block: (T) -> Unit) = ts.forEach(block)

