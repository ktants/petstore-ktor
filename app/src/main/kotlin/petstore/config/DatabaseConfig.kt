package petstore.config

import com.sksamuel.hoplite.Masked
import java.time.Duration
import java.sql.Connection as JdbcConnection

data class DatabaseConfig(
    val connection: Connection,
    val connectionPool: ConnectionPool,
    val schema: String? = "petstore-sp",
    val eagerInit: Boolean = DEFAULT_EAGER_INIT,
    val setupScript: String? = null,
    val tearDownScript: String? = null,
    val validateConfig: Boolean = true,
    val validateScripts: Boolean = true
) {

    data class ConnectTestStatements(
        val onAddingToPool: String? = null,
        val onReturnToPool:String? = null,
    )

    data class Connection(
        val username: String,
        val password: Masked,
        val jdbcUrl: String,
        val isolationLevel: IsolationLevel = IsolationLevel.TRANSACTION_REPEATABLE_READ,
        val driverClassName: String? = null,
        val autoCommit: Boolean = DEFAULT_AUTO_COMMIT,
    ) {
        enum class IsolationLevel(val constant: Int, val constantName: String) {
            TRANSACTION_READ_UNCOMMITTED(
                JdbcConnection.TRANSACTION_READ_UNCOMMITTED,
                "TRANSACTION_READ_UNCOMMITTED"
            ),
            TRANSACTION_READ_COMMITTED(
                JdbcConnection.TRANSACTION_READ_COMMITTED,
                "TRANSACTION_READ_COMMITTED"
            ),
            TRANSACTION_REPEATABLE_READ(
                JdbcConnection.TRANSACTION_REPEATABLE_READ,
                "TRANSACTION_REPEATABLE_READ"
            ),
            TRANSACTION_SERIALIZABLE(
                JdbcConnection.TRANSACTION_SERIALIZABLE,
                "TRANSACTION_SERIALIZABLE"
            )
        }
    }

    data class ConnectionPool(
        val minPoolSize: Int = DEFAULT_MIN_POOL_SIZE,
        val maxPoolSize: Int = DEFAULT_MAX_POOL_SIZE,
        val connectionTimeOut: Duration = Duration.ofSeconds(DEFAULT_CONNECTION_TIMEOUT_IN_SECONDS),
        val idleTimeOut: Duration = Duration.ofSeconds(DEFAULT_IDLE_TIMEOUT_IN_SECONDS),
        val allowPoolSuspension: Boolean = false,
        val test: ConnectTestStatements? = null,
        val poolName: String = "connection-pool-petstore"
    )

    companion object {
        const val DEFAULT_MIN_POOL_SIZE = 1
        const val DEFAULT_MAX_POOL_SIZE = 10
        const val DEFAULT_AUTO_COMMIT = false
        const val DEFAULT_IDLE_TIMEOUT_IN_SECONDS = 30L
        const val DEFAULT_CONNECTION_TIMEOUT_IN_SECONDS = 5L
        const val DEFAULT_EAGER_INIT = false
    }
}