package petstore

import io.ktor.server.application.*
import petstore.config.Config
import petstore.server.configureHttp
import petstore.server.configureApiRouting
import petstore.server.configureApplicationDatasource
import petstore.server.configureRouting
import petstore.server.configureSerialization
import io.ktor.server.cio.EngineMain as KtorCioEngine

fun main(args: Array<String>) = KtorCioEngine.main(args)

fun Application.module() {
    val config = Config.load()
    configureRouting()
    configureSerialization()
    configureHttp()
    configureRouting()
    configureApiRouting()
    configureApplicationDatasource(config.datasource)
}