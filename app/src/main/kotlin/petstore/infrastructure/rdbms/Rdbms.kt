@file:JvmName("Rdbms")

package petstore.infrastructure.rdbms

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import petsrore.commons.resourcesOf
import petstore.config.RdbmsConfig

fun Database.Companion.connect(config: RdbmsConfig): HikariDataSource {
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
    ).also { Database.connect(it) }
}

private fun RdbmsConfig.validateScripts() {
    fun validate(script: String?, whichScript: String) {
        require(script?.let { resourcesOf<RdbmsConfig>(script).isPresent } ?: return) {
            "[$whichScript] required, but not found: $script"
        }
    }
    validate(setupScript, "setupScript")
    validate(tearDownScript, "tearDownScript")
}


