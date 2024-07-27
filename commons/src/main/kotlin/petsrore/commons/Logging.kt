package petsrore.commons

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun logger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)