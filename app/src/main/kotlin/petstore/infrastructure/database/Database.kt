@file:JvmName("Rdbms")

package petstore.infrastructure.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import petstore.commons.resourcesOf
import petstore.config.DatabaseConfig

fun Database.Companion.connect(config: DatabaseConfig): HikariDataSource {
    return HikariDataSource(
        HikariConfig().apply {
            password = config.connection.password.value
            username = config.connection.username
            driverClassName = config.connection.driverClassName
            jdbcUrl = config.connection.jdbcUrl
            transactionIsolation = config.connection.isolationLevel.constantName
            isAllowPoolSuspension = config.connectionPool.allowPoolSuspension
            isAutoCommit = config.connection.autoCommit
            schema = config.schema
            poolName = config.connectionPool.poolName
            // Optional configs
            config.connectionPool.test?.onAddingToPool?.also(this::setConnectionInitSql)
            config.connectionPool.test?.onReturnToPool?.also(this::setConnectionTestQuery)
            if (config.validateConfig) validate()
            if (config.validateScripts) config.validateScripts()
        }
    ).also { connect(it) }
}

private fun DatabaseConfig.validateScripts() {
    fun validate(script: String?, whichScript: String) {
        require(script?.let { resourcesOf<DatabaseConfig>(script).isPresent } ?: return) {
            "[$whichScript] required, but not found: $script"
        }
    }
    validate(setupScript, "setupScript")
    validate(tearDownScript, "tearDownScript")
}


