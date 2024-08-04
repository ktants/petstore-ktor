package petstore.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import petsrore.commons.api.Pageable
import petsrore.commons.api.Response
import petstore.api.owner.OwnerApi
import petstore.api.owner.dto.NewOwnerDto

fun Application.configureApiRouting() {
    val defaultPageSize = 10
    val defaultPageNo = 1
    val ownersApi = OwnerApi()

    routing {
        route("/owners") {
            get() {
                val pageable = call.request.pageable(defaultPageNo, defaultPageSize)
                val query = call.request.queryParameters["query"]
                call.terminated(ownersApi.find(query, pageable))
            }
            get("/{id}") { call.terminated(ownersApi.ownerById(call.parameters["id"])) }
            post<NewOwnerDto> { call.terminated(ownersApi.createNew(it)) }
        }
        route("/services") {}
        route("/pets") {}
    }
}

private suspend fun ApplicationCall.terminated(response: Response<*>) =
    with(response) { respond(httpStatusCode(), this) }

private fun Response<*>.httpStatusCode(): HttpStatusCode {
    return when (this) {
        is Response.Error -> HttpStatusCode.BadRequest
        is Response.Part<*, *> -> HttpStatusCode.OK
        is Response.Single -> HttpStatusCode.OK
    }
}

private fun ApplicationRequest.pageable(defaultPageNo: Int, defaultPageSize: Int): Pageable =
    queryParameters.run {
        Pageable.of(
            pageNo = get("page")?.toInt() ?: defaultPageNo,
            pageSize = get("pageSize")?.toInt() ?: defaultPageSize,
        )
    }
