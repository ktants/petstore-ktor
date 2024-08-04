package petstore.server

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import petsrore.commons.ScriptBlock
import petsrore.commons.jdbc.connection
import petsrore.commons.resourcesOf
import petsrore.commons.scriptBlocks
import petstore.config.RdbmsConfig
import petstore.infrastructure.rdbms.connect
import kotlin.jvm.optionals.getOrNull

fun Application.configureApplicationDatasource(config: RdbmsConfig) {
    Database.connect(config).also { datasource ->
        fun tearDn(a: Any?) {
            config.tearDownScript?.let { resourcesOf<RdbmsConfig>(it) }?.getOrNull()?.runCatching {
                openStream().bufferedReader().scriptBlocks {
                    datasource.connection().use {
                    }
                    ScriptBlock.ProcessControl.CONTINUE
                }
            }
        }

        fun setUp(a: Any?) {}
        environment.monitor.subscribe(ApplicationStopping, ::tearDn)
        environment.monitor.subscribe(ApplicationStarting, ::setUp)
    }
}


