package petstore.commons

import java.io.File
import java.net.URL
import java.util.Optional

inline fun <reified T> resourcesOf(resource: String): Optional<URL> =
    when {
        resource.startsWith("classpath:") ->
            T::class.java.getResource(resource.substringAfter("classpath:"))

        resource.startsWith("file:") ->
            File(resource.substringAfter("file:")).takeIf { it.exists() }?.toURI()?.toURL()

        else ->
            T::class.java.getResource(resource)
    }.optional()

