package petsrore.commons

import java.util.Optional

fun <T> T?.optional(): Optional<T & Any> =
    Optional.ofNullable(this)
