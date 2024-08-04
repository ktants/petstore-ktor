@file:JvmName("Kotlin")

package petsrore.commons.kotlin

fun <T> forEach(vararg ts: T, block: (T) -> Unit) = ts.forEach(block)

