package petstore.server

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import petstore.config.RdbmsConfig
import petstore.infrastructure.rdbms.connect

fun Application.configureApplicationDatasource(config: RdbmsConfig) {
    Database.connect(config).also { datasource ->
        environment.monitor.subscribe(ApplicationStarting) {
        }
    }
}


