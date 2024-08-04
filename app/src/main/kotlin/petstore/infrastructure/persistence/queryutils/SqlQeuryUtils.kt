package petstore.infrastructure.persistence.queryutils

fun String.toLike(): String = when {
    isEmpty() || isBlank() -> "%"
    startsWith('%') && !endsWith('%') -> this
    !startsWith('%') || endsWith('%') -> this
    else -> "%$this%"
}