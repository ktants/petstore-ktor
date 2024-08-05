package petstore.server

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import petstore.config.DatabaseConfig
import petstore.infrastructure.database.connect

fun Application.configureApplicationDatasource(config: DatabaseConfig) {
    Database.connect(config).also { datasource ->
        environment.monitor.subscribe(ApplicationStarting) {
        }
    }
}


