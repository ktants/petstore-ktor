package petstore

import com.sksamuel.hoplite.ExperimentalHoplite
import io.ktor.server.application.*
import petstore.config.Config
import petstore.server.configureHttp
import petstore.server.configureApiRouting
import petstore.server.configureDatasource
import petstore.server.configureStaticRouting
import petstore.server.configureSerialization
import io.ktor.server.cio.EngineMain as KtorCioEngine

fun main(args: Array<String>) = KtorCioEngine.main(args)

@ExperimentalHoplite
fun Application.module() {
    val config = Config.load()
    configureDatasource(config.datasource)
    configureStaticRouting()
    configureSerialization()
    configureHttp()
    configureApiRouting(config.api)
}