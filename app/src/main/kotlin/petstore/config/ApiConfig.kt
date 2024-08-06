package petstore.config

data class ApiConfig(
    val version: Int = 1,
    val defaultPageSize: Int = 10,
)