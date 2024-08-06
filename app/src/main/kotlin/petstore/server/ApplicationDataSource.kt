package petstore.server

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import petstore.commons.debug
import petstore.commons.info
import petstore.commons.logger
import petstore.commons.warn
import petstore.config.DatabaseConfig
import petstore.infrastructure.database.connect

val logger = logger("petstore.server.ApplicationDataSource")

fun Application.configureDatasource(c: DatabaseConfig) {
    logger.debug { "setting up datasource: ${c.connection.jdbcUrl}" }
    val subscriber = environment.monitor.subscribe(ApplicationStarting) { Database.connect(c) }
    environment.monitor.unsubscribe(ApplicationStopped) {

        subscriber.runCatching { dispose() }.exceptionOrNull()
            ?.also { logger.warn(it) { "disposing connection subscription failed due to an exception." } }

        runCatching { disposeConnectedDatabase() }.exceptionOrNull()
            ?.also { logger.warn(it) { "disposing connected database failed due to an exception." } }

        logger.info { "application stopped" }
    }
}

private fun disposeConnectedDatabase() {
    TransactionManager.closeAndUnregister(TransactionManager.defaultDatabase ?: return)
}


