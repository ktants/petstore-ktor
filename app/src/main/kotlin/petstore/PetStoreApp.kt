package petstore

import io.ktor.server.application.*
import petstore.server.configureHttp
import petstore.server.configureRouting
import petstore.server.configureSerialization
import io.ktor.server.cio.EngineMain as KtorCioEngine

fun main(args: Array<String>) = KtorCioEngine.main(args)

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureHttp()
}