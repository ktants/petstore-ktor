package petstore.commons

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun logger(clazz: Class<*>): Logger = LoggerFactory.getLogger(clazz)

fun logger(name: String): Logger = LoggerFactory.getLogger(name)

fun Logger.debug(message: () -> Any?) {
    if (isDebugEnabled) {
        debug(message().toString())
    }
}

fun Logger.info(message: () -> Any?) {
    if (isInfoEnabled) {
        info(message().toString())
    }
}

fun Logger.error(message: () -> Any?) {
    if (isErrorEnabled) {
        error(message().toString())
    }
}

fun Logger.warn(cause: Throwable, message: () -> Any?) {
    if (isWarnEnabled) {
        warn(message().toString(), cause)
    }
}